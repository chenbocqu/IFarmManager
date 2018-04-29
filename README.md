# IFarmManager

    智能农场后台管理

## 1. 介绍

    用于设备的添加与管理

# 功能

## 1、设备生产，向服务器请求所有的设备类别和类型

    http://localhost:8080/IFarm/device/category

返回

    array：[{
        "deviceCategory": "collectorDevice",
        "deviceCategoryName": "采集设备",
        "deviceType": [{
            "deviceType": "collectorType5",
            "deviceTypeName": "五合一采集设备"
        }]
    }, {
        "deviceCategory": "controlDevice",
        "deviceCategoryName": "控制设备",
        "deviceType": [{
            "deviceType": "type1",
            "deviceTypeName": "一路输出设备"
        }, {
            "deviceType": "type2",
            "deviceTypeName": "两路输出设备"
        }, {
            "deviceType": "type3",
            "deviceTypeName": "四路输出设备"
        }]
    }, {
        "deviceCategory": "concentrator",
        "deviceCategoryName": "集中器",
        "deviceType": [{
            "deviceType": "concentrator",
            "deviceTypeName": "集中器"
        }]
    }]

## 2、获得服务器生成的设备的id和校验

    http://localhost:8080/IFarm/device/production?batch=1&deviceCategory=controlDevice&deviceType=type1

返回

    {
        "response": "success", //"error"即为失败，不会有devices信息
        "devices": [{
            "deviceId": 115174041,  //设备编号
            "deviceVerification": "ydsopF7NVf",  //验证码
            "createTime": "2018-01-31 13:47:04.0"  //创建时间
        }]
    }

当batch=3时，批量生产

    {
        "response": "success",
        "devices": [{
            "deviceId": 822724144,
            "deviceVerification": "FlYz9qrnTb",
            "createTime": "2018-01-31 13:48:49.0"
        }, {
            "deviceId": 909811140,
            "deviceVerification": "YtMhcfZgDx",
            "createTime": "2018-01-31 13:48:49.0"
        }, {
            "deviceId": 151831267,
            "deviceVerification": "AKR3C7mU32",
            "createTime": "2018-01-31 13:48:49.0"
        }]
    }



## 3、用户添加管理人员（需要token验证）

user目前权限分为四级:

|模式|图片|备注
|---|---|---|
|farmer|农场主|无|
|vipFarmer|vip农场主|可以开通三个以后的农场管理人员|
|onlySee|只能查看农场数据|无|
|doControl|可以操作控制|无|

后续还会开通visitor模式，如果是onlySee的用户在系统控制界面，系统会返回

    "no_auth"

目前系统的控制也需要权限认证，否则不能通过

    http://localhost:8080/IFarm/user/addSubUser?userId=00000000000&farmId=10000001&authority=onlySee

返回

    {"response":"success"}  success:成功，error：失败，full_subUser:超过3个人 no_auth:没有权限


## 4、集中器添加

    http://localhost:8080/IFarm/device/concentrator/addition

post参数：

    {
    collectorId：long，
    farmId：int,  //通过userId去查找对应的farmId
    collectorLocation:String,
    collectorType:String, //环境数据采集集中器  和  终端设备控制集中器
    collectorVersion:String,
    }

返回结果：

    {"response":"success"} 或者
    {"response":"error"}

## 5、采集设备添加

    http://localhost:8080/IFarm/device/collectorDevice/addition

post参数：

    {
    collectorId：long，手动输入
    farmId：int,  //通过userId去查找对应的farmId
    deviceVersion：String，
    deviceType：String ， //从farmCollectorDevice/collectorDeviceTypeList获得，见以前文档
    deviceDistrict：String，//设备分区
    deviceLocation：String,//设备位置
    }

返回结果：

    {"response":"success"} 或者
    {"response":"error"}


## 6、控制设备添加

    http://localhost:8080/IFarm/device/controlDevice/addition

post参数：

    {
    collectorId：long，手动输入
    deviceType：String ，
    deviceDescription：String，//设备描述
    deviceLocation：String,//设备位置
    }

返回结果：

    {"response":"success"} 或者
    {"response":"error"}

## 7、控制系统添加

    http://localhost:8080/IFarm/farmControlSystem/addition

post参数：

    {
    farmId：int,
    systemCode:String,
    systemType:String,
    systemTypeCode:String, //这三个可以从farmControlSystem/type获取
    systemDistrict：String ，分区
    systemNo：String，//系统编号，如1号控制系统
    systemLocation：String,//系统位置
    systemDescription：String
    }


## 8、水肥药一体化系统添加

    http://localhost:8080/IFarm/farmControlSystem/wfm/addition

post参数：

    {
    farmId：int,
    systemCode:String, //waterFertilizerMedicine
    systemType:String, //水肥药系统
    systemTypeCode:String, //waterFertilizerMedicineControl
    systemDistrict：String ，分区
    systemNo：String，//系统编号，如1号控制系统
    medicineNum:int,//药罐个数
    districtNum：int，//区域数
    fertierNum：int，//肥罐数
    systemLocation：String,//系统位置
    systemDescription：String
    }

返回结果：

    {"response":"success"} 或者
    {"response":"error"}


## 9、控制系统终端添加（较复杂）

    http://localhost:8080/IFarm/farmControl/terminal/addition

post参数：

    {
    controlDeviceId：int, //控制设备Id
    systemId:int, //控制系统id
    controlDeviceBit:int, //这个终端对应设备的第几路输出，从0开始
    controlType:String, //来源于controlSystem的查询
    functionName：String ，
    functionCode：String，//这两个可以从farmControlSystem/terminalType获取
    terminalIdentifying:String /系统终端标识
    }

返回结果：

    {"response":"success"} 或者
    {"response":"error"}

## 10、水肥一体化系统终端添加

    http://localhost:8080/IFarm/farmControl/terminal/addition

post参数：

    {
    controlDeviceId：int, //控制设备Id
    systemId:int, //控制系统id
    controlDeviceBit:int, //这个终端对应设备的第几路输出，从0开始
    controlType:String, //来源于controlSystem的查询
    functionName：String ，
    functionCode：String，
    terminalIdentifying:String //这三个可以从farmControlSystem/wfm/terminalType获取
    }

返回结果：

    {"response":"success"} 或者
    {"response":"error"}


## 11、管理人员登陆

    http://localhost:8080/IFarm/manager/login?managerId=2011&managerPwd=123456

返回

    {"response":"success","token":"...."} 或者
    {"response":"error"}

## 12、设备和服务器验证

    http://localhost:8080/IFarm/device/check?deviceId= & deviceVerification=?

返回

    {"response":"success","device":"...."} 或者
    {"response":"no_id"} 或{"response":"check_error"}

## 13、农场主用户查询自己的下属人员信息

    http://localhost:8080/IFarm/user/subUserQuery?userId=00000000000&signature=ifarmb02ba08a9fbf471eabebb6ccf03aff07MDAwMDAwMDAwMDA=

返回jsonarray，字段是user的字段

    [{
        "userId": "00000000000_101832",
        "userPwd": "123456",
        "userRegisterTime": "2018-03-04 17:15:04",
        "userLastLoginTime": "2018-03-04 17:15:04"
    }]
    其他字段为null就没有

## 14、农场主查询自己的下属人员的权限

    http://localhost:8080/IFarm/user/subUserAuthorityQuery?userId=00000000000&signature=ifarmb02ba08a9fbf471eabebb6ccf03aff07MDAwMDAwMDAwMDA=

返回:

    [{
    	"authId": "1",
    	"userId": "00000000000_101832",
    	"farmId": "10000001",
    	"authority": "onlySee"
    }]

## 15、管理员查询所有用户

    http://localhost:8080/IFarm/manager/allUser?managerId=2011&token=ifarm320df0e49af94e52811909665b531e4fMjAxMQ==

返回：

    [{
    	"userId": "00000000000",
    	"userName": "余先生",
    	"userPwd": "123456",
    	"userSex": "男",
    	"userRegisterTime": "2017-03-31 15:32:28",
    	"userLastLoginTime": "2018-01-21 15:56:45",
    	"userBackImageUrl": "http://127.0.0.1:8080/IFarm/images/users/00000000000/9/0/849682f4-b260-4ef2-a32c-8b09d9a16484_00000000000_背景.png",
    	"userImageUrl": "http://127.0.0.1:8080/IFarm/images/users/00000000000/13/13/0c8472ac-46c1-4c0b-99d9-d871ec1a21bf_00000000000_头像.png",
    	"userSignature": "智能生态农场，联系方式：18323433566",
    	"userRole": "farmer"
    }, {
    	"userId": "00000000000_101832",
    	"userPwd": "123456",
    	"userRegisterTime": "2018-03-04 17:15:04",
    	"userLastLoginTime": "2018-03-04 17:15:04"
    }, {
    	"userId": "13167952068",
    	"userName": "用户13167952068",
    	"userPwd": "123456",
    	"userSex": "男",
    	"userRegisterTime": "2017-02-21 12:04:12",
    	"userLastLoginTime": "2017-02-21 12:04:35",
    	"userBackImageUrl": "http://127.0.0.1:8080/IFarm/images/users/13167952068/3/11/3f382963-933e-4fff-a256-389a5fa9fa40_13167952068_背景.png",
    	"userImageUrl": "http://127.0.0.1:8080/IFarm/images/users/13167952068/13/11/9e14d1cd-1f9d-49e3-899c-9910b62665e5_13167952068_头像.png",
    	"userSignature": "这个家伙很懒，没有任何介绍！"
    }, {
    	"userId": "13594039472",
    	"userName": "hashmap",
    	"userPwd": "123456",
    	"userSex": "男",
    	"userRegisterTime": "2017-03-31 16:02:06",
    	"userLastLoginTime": "2017-04-01 16:51:01",
    	"userBackImageUrl": "http://127.0.0.1:8080/IFarm/images/users/13594039472/4/13/7f506c88-7c36-4443-a536-2ff76dcd9710_13594039472_背景.png",
    	"userImageUrl": "http://127.0.0.1:8080/IFarm/images/users/13594039472/3/3/98b5c621-23a6-438c-8da6-c2dfca8fcf14_13594039472_头像.png",
    	"userSignature": "这个家伙很懒，没有任何介绍！"
    }, {
    	"userId": "13594039472_123562",
    	"userName": "小豆子 ",
    	"userPwd": "123456",
    	"userSex": "女",
    	"userRegisterTime": "2017-01-05 13:27:50",
    	"userLastLoginTime": "2017-04-01 15:57:57",
    	"userBackImageUrl": "http://127.0.0.1:8080/IFarm/images/users/13594039472_123562/3/5/76cc79f6-bb7e-4b86-b5ac-8ef4503268a8_18883701030_背景.png",
    	"userImageUrl": "http://127.0.0.1:8080/IFarm/images/users/13594039472_123562/2/13/2f6be143-1fe4-4e08-90f2-df4e29cd8ffa_18883701030_头像.png",
    	"userSignature": "既然选择了远方，并只顾风雨兼程！"
    }, {
    	"userId": "15025316896",
    	"userName": "大大",
    	"userPwd": "123456",
    	"userSex": "女",
    	"userRegisterTime": "2017-01-03 12:00:02",
    	"userLastLoginTime": "2017-06-06 20:41:07",
    	"userBackImageUrl": "http://127.0.0.1:8080/IFarm/images/users/15025316896/6/1/fe83c08c-9e61-44ee-9fe6-1609ea80307e_15025316896_背景.png",
    	"userImageUrl": "http://127.0.0.1:8080/IFarm/images/users/15025316896/12/8/dff6813c-8a3d-469d-bc8b-4645154fc2d5_15025316896_头像.png",
    	"userSignature": "生活不容易的！"
    }, {
    	"userId": "17323605777",
    	"userName": "用户17323605777",
    	"userPwd": "123456",
    	"userSex": "男",
    	"userRegisterTime": "2017-03-30 16:18:50",
    	"userLastLoginTime": "2017-03-30 16:19:07",
    	"userSignature": "这个家伙很懒，没有任何介绍！"
    }, {
    	"userId": "18323433566",
    	"userName": "用户18323433566",
    	"userPwd": "0987654321",
    	"userSex": "男",
    	"userRegisterTime": "2017-03-26 10:03:32",
    	"userLastLoginTime": "2017-04-06 23:19:31",
    	"userSignature": "这个家伙很懒，没有任何介绍！"
    }, {
    	"userId": "18795632458",
    	"userName": "张三",
    	"userPwd": "123456",
    	"userSex": "男",
    	"userRegisterTime": "2017-01-04 12:00:06",
    	"userLastLoginTime": "2017-02-16 22:12:23",
    	"userBackImageUrl": "http://127.0.0.1:8080/IFarm/images/users/18795632458/2/10/77e928eb-7c30-4d6b-96e5-93b543c02f57_18795632458_背景.png",
    	"userImageUrl": "http://127.0.0.1:8080/IFarm/images/users/18795632458/15/12/0d15eff4-6277-4fd7-bce9-22a21f8272bc_18795632458_头像.png",
    	"userSignature": "生活是美好的，相信自己！"
    }, {
    	"userId": "18883384950",
    	"userName": "李四",
    	"userPwd": "123456",
    	"userSex": "男",
    	"userRegisterTime": "2017-02-21 11:20:43",
    	"userLastLoginTime": "2017-02-21 11:51:40",
    	"userBackImageUrl": "http://127.0.0.1:8080/IFarm/images/users/18883384950/7/6/6ebd99f4-e044-4e63-80c0-615e62966427_18883384950_背景.png",
    	"userImageUrl": "http://127.0.0.1:8080/IFarm/images/users/18883384950/12/5/916a06bb-8057-46cd-a62d-cc79729755ef_18883384950_头像.png",
    	"userSignature": "学会生活，面对压力！"
    }]

## 16、更新下属人员权限

    http://localhost:8080/IFarm/user/subUserAuthorityUpdate?userId=00000000000&signature=ifarmb02ba08a9fbf471eabebb6ccf03aff07MDAwMDAwMDAwMDA=&authId=1&authority=all

返回:

    {"response":"success"}