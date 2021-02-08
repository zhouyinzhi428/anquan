package com.nanjingtaibai.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nanjingtaibai.system.entity.Mcgs;
import com.nanjingtaibai.system.entity.OpcItemValue;
import com.nanjingtaibai.system.mapper.McgsMapper;
import com.nanjingtaibai.system.mapper.OpcItemValueMapper;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.WxCpMessageSendResult;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/weixin/")
public class WeiXinController {
    @Autowired
    private McgsMapper mcgsMapper;
    @Autowired
    private OpcItemValueMapper opcItemValueMapper;

    public void sendMessage(String desc,String name,Float val) throws WxErrorException {
        String dataStr="["+desc+"]"+"\n位号为："+name+"\n当前值为："+val;
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId("wx1dcf93e7821466c6");      // 设置微信企业号的appid
        config.setCorpSecret("zwdSI5-iwk1lkeghz3HOnXvQHeOdLAunwH5Mu_edjlU");  // 设置微信企业号的app corpSecret
        WxCpServiceImpl wxCpService=new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);

        WxCpMessage message = WxCpMessage.TEXT().agentId(2).toUser("zhouyinzhi").content(dataStr).build();
        WxCpMessageSendResult send = wxCpService.messageSend(message);
        System.out.println(send);
    }
    @ApiOperation(value = "测试发送消息" ,notes = "测试发送消息")
    @GetMapping("handleSendMessage")
    @Scheduled(cron = "0 */10 * * * ?")
    @Async
    public void handleSendMessage() throws WxErrorException {
        List<String> list = new ArrayList<>();
        list.add("F_SO2");
        list.add("F_NO");
        list.add("F_SO2B");
        list.add("F_NOB");
        list.add("TE10_013");
        list.add("TE10_014");
        QueryWrapper<OpcItemValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("POINT_NAME", list);
        List<OpcItemValue> opcItemValueList = opcItemValueMapper.selectList(queryWrapper);
        Mcgs mcgs=mcgsMapper.getData();
        Float codVal=mcgs.getWpv19();
        Float zongdanVal=mcgs.getWpv7();
        Float andanVal=mcgs.getWpv15();
        Float zonglingVal=mcgs.getWpv8();
        Float zonglingVal_nk=mcgs.getWpv29();
        Float qsphVal=mcgs.getWpv18();
        Float nyywwdVal=getVal("TE10_013",opcItemValueList);
        Float byywwdVal=getVal("TE10_014",opcItemValueList);
        Float nydyhwVal=getVal("F_NOB",opcItemValueList);
        Float nyso2Val=getVal("F_SO2B",opcItemValueList);
        Float bydyhwVal=getVal("F_NO",opcItemValueList);
        Float byso2Val=getVal("F_SO2",opcItemValueList);

        if(codVal>35){
            sendMessage("COD报警","wpv19",codVal);
        }
        if(zongdanVal>12){
            sendMessage("总氮报警","wpv7",zongdanVal);
        }
        if(andanVal>4){
            sendMessage("氨氮报警","wpv15",andanVal);
        }
        if(zonglingVal>0.45){
            sendMessage("总磷报警","wpv8",zonglingVal);
        }
        if(zonglingVal_nk>0.45){
            sendMessage("内控总磷报警","wpv8",zonglingVal_nk);
        }
        if(qsphVal>8.5 || qsphVal<6.5){
            sendMessage("清水池出口总管PH","wpv8",qsphVal);
        }
        if(nyywwdVal>420){
            sendMessage("南窑窑尾温度报警","TE10_013",nyywwdVal);
        }
        if(byywwdVal>420){
            sendMessage("北窑窑尾温度报警","TE10_014",byywwdVal);
        }
        if(nydyhwVal>120){
            sendMessage("南窑氮氧化合物含量报警","F_NOB",nydyhwVal);
        }
        if(nyso2Val>60){
            sendMessage("南窑SO2含量报警","F_SO2B",nyso2Val);
        }
        if(bydyhwVal>120){
            sendMessage("北窑氮氧化合物含量报警","F_NOB",bydyhwVal);
        }
        if(byso2Val>60){
            sendMessage("北窑SO2含量报警","F_SO2B",byso2Val);
        }


    }
    public Float getVal(String pointname,List<OpcItemValue> opcItemValueList){
        Optional<OpcItemValue> cartOptional = opcItemValueList.stream().filter(item -> item.getPointname().equals(pointname)).findFirst();
        Float val=null;
        if (cartOptional.isPresent()) {
            OpcItemValue opcItemValue = cartOptional.get();
            Float fvalue = opcItemValue.getFvalue();
            val=fvalue;
        } else {
            val=null;
        }
        return val;
    }
}
