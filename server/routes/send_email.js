
module.exports = function(router,app) {
    console.log('send_email 호출');
    var nodemailer = require('nodemailer');
    var transporter = nodemailer.createTransport({
        service: 'naver',
        auth: {
            user: 'osang915@naver.com',
            pass: '@dhtkd0915'}
        });
        

    router.route('/send_email').post(function(req,res){
        console.log('email 인증 전송 요청');
        var email = req.body.email;
        var database = app.get('database');
        database.UserModel.findOne({ 'email' :  email }, function(err, user) {
            //user.randomvalue=(Math.floor(Math.random()*(999999-100000))+100000).toString();
            user.save(function(err){
                if(err){
                    throw err;
                }
                //console.log('user randomvalue 생성');
            });
            var mailOptions = {
                from: 'osang915@naver.com',
                to: email,    
                subject: '니꺼내꺼 내꺼니꺼 인증 메일',
                //text: user.randomvalue
                html: '<table lang="en" width="100%" height="100%" style="min-width: 348px;" border="0" cellspacing="0" cellpadding="0"><tbody><tr height="32" style="height: 32px;"><td></td></tr><tr align="center"><td><table border="0" cellspacing="0" cellpadding="0" style="padding-bottom: 20px; max-width: 516px; min-width: 220px;"><tbody><tr><td width="8" style="width: 8px;"></td><td><div align="center" style="border-style: solid; border-width: thin; border-color:#dadce0; border-radius: 8px; padding: 40px 20px;"><img src="https://ifh.cc/g/47UJX.png" width="200" height="100" style="margin-bottom: 16px;"><div style="font-family: Google Sans,Roboto,RobotoDraft,Helvetica,Arial,sans-serif;border-bottom: thin solid #dadce0; color: rgba(0,0,0,0.87); line-height: 32px; padding-bottom: 24px;text-align: center; word-break: break-word;"><div style="font-size: 24px;">회원가입 완료를 위한 인증</div><table align="center" style="margin-top:8px;"><tbody><tr style="line-height: normal;"><td align="right" style="padding-right:8px;"><img width="20" height="20" style="width: 20px; height: 20px; vertical-align: sub; border-radius: 50%;;" src="https://www.gstatic.com/accountalerts/email/anonymous_profile_photo.png"></td><td><a style="font-family: google sans,roboto,robotodraft,helvetica,arial,sans-serif;color: rgba(0,0,0,0.87); font-size: 14px; line-: 20px;" rel="noreferrer noopener" target="_blank">'+email+'</a></td></tr></tbody></table></div><div style="font-family: Roboto-Regular,Helvetica,Arial,sans-serif; font-size: 14px; color: rgba(0,0,0,0.87); line-height: 20px;padding-top: 20px; text-align: center;">니꺼내꺼 내꺼니꺼 서비스의 회원가입을 완료 하시려면<br> 아래의 인증 버튼을 눌러주세요.<div style="padding-top: 32px; text-align: center;"><a href="http://ec2-15-164-51-129.ap-northeast-2.compute.amazonaws.com:3000/authentication_email?email='+email+'" target="_blank" style="font-family: google sans,roboto,robotodraft,helvetica,arial,sans-serif; line-: 16px; color: #ffffff; font-weight: 400; text-decoration: none;font-size: 14px;display:inline-block;padding: 10px 24px;background-color: #4184f3; border-radius: 5px; min-: 90px;" rel="noreferrer noopener">인증</a></div></div></div><div style="text-align: left;"><div style="font-family: Roboto-Regular,Helvetica,Arial,sans-serif;color: rgba(0,0,0,0.54); font-size: 11px; line-height: 18px; padding-top: 12px; text-align: center;"><div>이 이메일은 니꺼내꺼 내꺼니꺼 서비스의 가입 인증을 위해 발송합니다.</div></div></div></td><td width="8" style="width: 8px;"></td></tr></tbody></table></td></tr><tr height="32" style="height: 32px;"><td></td></tr></tbody></table>'
                };
            transporter.sendMail(mailOptions,(err,responses)=>{

                if(err){
                    throw err;
                }
                res.send('email true');
                
                //res.send(user.randomvalue);
                //res.send('email 발송 성공했습니다.');
                transporter.close();
            });
        
        });

    });
};

