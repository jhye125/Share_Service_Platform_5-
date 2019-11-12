
module.exports = function(router, passport) {
    console.log('user_passport 호출됨.');

    
    // true
    router.route('/login_true').get(function(req, res) {
        console.log('/login_true 패스 요청됨.');
        res.send("true");
    });
    //false
  
    router.route('/login_false').get(function(req, res) {
        console.log('/login_false 패스 요청됨.');
        var send_data= 'false';
        for(var key in req.sessionStore.sessions){
            console.log(key);
        }
        var value = req.sessionStore.sessions[key];
        console.log(value);
        if(value.indexOf('email 인증을 완료해주세요.') != -1){
            send_data = 'email 인증을 완료해주세요.';
        }else if(value.indexOf('email을 확인해주세요.') != -1){
            send_data = 'email을 확인해주세요.';
        }else if(value.indexOf('password를 확인해주세요.')!=-1){
            send_data = 'password를 확인해주세요.';
        }
        res.send(send_data);
        
    }); 
    
    
    // true
    router.route('/signup_true').get(function(req, res) {
        console.log('/signup_true 패스 요청됨.');
        res.send("true");
    });
    //false
    router.route('/signup_false').get(function(req, res) {
        console.log('/signup_false 패스 요청됨.');
        var send_data2= 'false';
        for(var key in req.sessionStore.sessions){
            console.log(key);
        }
        var value = req.sessionStore.sessions[key];
        console.log(value);
        if(value.indexOf('계정이 이미 있습니다.') != -1){
            send_data2 = '계정이 이미 있습니다.';
        }
        res.send(send_data2);
        
    });


    // 로그인 인증
    router.route('/login').post(passport.authenticate('local-login', {
        successRedirect : '/login_true', 
        failureRedirect : '/login_false', 
        failureFlash: true
    }));
    

    // 회원가입 인증
    router.route('/signup').post(passport.authenticate('local-signup', {
        successRedirect : '/signup_true', 
        failureRedirect : '/signup_false', 
        failureFlash : true 
    }));
/*
    // 패스포트 - 구글
    router.route('/auth/google').get(passport.authenticate('google', { 
        scope : 'email' 
    }));

    // 패스포트 - 구글
    router.route('/auth/google/callback').get(passport.authenticate('google', {
        successRedirect : '/true',
        failureRedirect : '/false'
    }));
*/
};
