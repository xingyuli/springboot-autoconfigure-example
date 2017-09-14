package com.example.app.controller

import com.example.app.dto.SpringBeanResource
import com.example.app.dto.beanResource
import com.mobisist.components.messaging.wechatpush.TemplateData
import com.mobisist.components.messaging.wechatpush.WechatPushMessage
import com.mobisist.components.messaging.wechatpush.WechatPushMessagingException
import com.mobisist.components.messaging.wechatpush.WechatPushSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/wechatPush")
open class WechatPushController {

    @Autowired
    private lateinit var wechatPushSender: WechatPushSender

    @GetMapping("/configuredBean")
    fun getConfiguredBean(): SpringBeanResource = wechatPushSender.beanResource("wechatPushSender")

    /*
      标题: 充值通知
      详细内容:
          {{first.DATA}}

          {{accountType.DATA}}：{{account.DATA}}
          充值金额：{{amount.DATA}}
          充值状态：{{result.DATA}}
          {{remark.DATA}}
    */

    @GetMapping("/demo/send_1")
    fun demoSend_1() {
        // un-typesafe, error prone, and unmaintainable
        val msg = WechatPushMessage("og5Wus9Do8qFisOVMSPMQmlH69sg", "recharge.id", "http://page.when.user.click.the.message").apply {
            templateData["first"] = TemplateData("充值成功！")
            templateData["accountType"] = TemplateData("钻石卡")
            templateData["account"] = TemplateData("No.0123456")
            templateData["amount"] = TemplateData("￥100")
            templateData["result"] = TemplateData("成功")
            templateData["remark"] = TemplateData("任何疑问请来咨询028-88888888")
        }

        try {
            wechatPushSender.send(msg)
        } catch (e: WechatPushMessagingException) {
            // you can handle exception here
        }
    }

    @GetMapping("/demo/send_2")
    fun demoSend_2() {
        // RECOMMENDED: manage all templates in one location, and more typesafe
        val msg = WechatPushTemplate.RechargeTemplate("og5Wus9Do8qFisOVMSPMQmlH69sg", "http://page.when.user.click.the.message").apply {
            first = TemplateData("充值成功！")
            accountType = TemplateData("钻石卡")
            account = TemplateData("No.0123456")
            amount = TemplateData("￥100")
            result = TemplateData("成功")
            remark = TemplateData("任何疑问请来咨询028-88888888")
        }

        try {
            wechatPushSender.send(msg)
        } catch (e: WechatPushMessagingException) {
            // you can handle exception here
        }
    }

    sealed class WechatPushTemplate(templateKey: String, openId: String, url: String) : WechatPushMessage(openId = openId, templateKey = templateKey, url = url) {

        class RechargeTemplate(openId: String, url: String = "") : WechatPushTemplate("recharge.id", openId, url) {
            var first: TemplateData by templateData
            var accountType: TemplateData by templateData
            var account: TemplateData by templateData
            var amount: TemplateData by templateData
            var result: TemplateData by templateData
            var remark: TemplateData by templateData
        }

        // more templates could be defined here...

    }

}