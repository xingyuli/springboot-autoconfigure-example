package com.example.app.controller

import com.baidu.yun.push.model.PushRequest
import com.example.app.dto.SpringBeanResource
import com.example.app.dto.beanResource
import com.mobisist.components.messaging.baidupush.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/baiduPush")
open class BaiduPushController {

    @Autowired
    private lateinit var baiduPushSender: BaiduPushSender

    @GetMapping("/configuredBean")
    fun getConfiguredBean(): SpringBeanResource = baiduPushSender.beanResource("baiduPushSender")

    @GetMapping("/demo/sendAndroid_1")
    fun demoSendAndroid_1(@RequestParam channelId: String) {
        // We have two configurations named:
        //   foo - enabled: true
        //   bar - enabled: false
        // bar is disabled, and suitable for demo

        // The config defaults to 'default' when constructing a message, which is
        // naturally used in outermost configuration approach. As we are using
        // the multi-configurations approach, specify it explicitly:
        val msg = baiduPushSender.buildMsgToAndroid("bar") {
            pushMsgToSingleDeviceRequest {
                this.channelId = channelId
                messageType = MessageType.NOTIFICATION.rawValue
                // 5 mins
                msgExpires = 300

                title = "Title goes here"
                description = "Description goes here"

                // customized body when send to android devices
                custom_content = mapOf(
                        "myKey" to "myValue"
                )
            }
        }

        try {
            baiduPushSender.send(msg)
        } catch (e: BaiduPushMessagingException) {
            // you can handle exception here
        }
    }

    @GetMapping("/demo/sendIos_1")
    fun demoSendIos_1() {
        val msg = baiduPushSender.buildMsgToIOS("bar") {
            pushMsgToSingleDeviceRequest {
                channelId = "3478330428537063857"
                messageType = MessageType.NOTIFICATION.rawValue
                // 5 mins
                msgExpires = 300

                alert = "Title goes here"
                sound = "default"

                "key1" setTo "value1"
                "key2" setTo "value2"
            }
        }

        try {
            baiduPushSender.send(msg)
        } catch (e: BaiduPushMessagingException) {
            // you can handle exception here
        }
    }

}