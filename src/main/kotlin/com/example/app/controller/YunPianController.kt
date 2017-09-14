package com.example.app.controller

import com.example.app.dto.SpringBeanResource
import com.example.app.dto.beanResource
import com.mobisist.components.messaging.sms.SmsMessage
import com.mobisist.components.messaging.sms.SmsMessagingException
import com.mobisist.components.messaging.sms.yunpian.v1.YunPianSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/yunPian")
class YunPianController {

    @Autowired
    private lateinit var yunPianSender: YunPianSender

    @GetMapping("/configuredBean")
    fun getConfiguredBean(): SpringBeanResource = yunPianSender.beanResource("baiduPushSender")

    @GetMapping("/demo/send_1")
    fun demoSend_1() {
        // un-typesafe, error prone, and unmaintainable()
        val code = "1234"
        val expireAfterMinutes = 15
        val msg = SmsMessage("17713571014", "【AppSignature】您的验证码是$code, 有效期为${expireAfterMinutes}分钟。")

        try {
            yunPianSender.send(msg)
        } catch (e: SmsMessagingException) {
            // you can handle exception here
        }
    }

    @GetMapping("/demo/send_2")
    fun demoSend_2() {
        // RECOMMENDED: manage all templates in one location, and more typesafe
        val msg = YunPianTemplate.VerificationCodeTemplate("17713571014", "1234", 15)

        try {
            yunPianSender.send(msg)
        } catch (e: SmsMessagingException) {
            // you can handle exception here
        }
    }

    sealed class YunPianTemplate(mobile: String, text: String) : SmsMessage(mobile, text) {

        class VerificationCodeTemplate(mobile: String, code: String, expireAfterMinutes: Long) : YunPianTemplate(
                mobile, "【AppSignature】您的验证码是$code, 有效期为${expireAfterMinutes}分钟。")

//        class VerificationCodeTemplate(mobile: String, code: String, expireAfterMinutes: Long) : YunPianTemplate(
//                mobile, "【养生达人】您的验证码是$code, 有效期为${expireAfterMinutes}分钟。")

        // more templates could be defined here...

    }

}