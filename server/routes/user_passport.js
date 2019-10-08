
module.exports = function(router, passport) {
    console.log('user_passport 호출됨.');


    // true
    router.route('/true').get(function(req, res) {
        console.log('/true 패스 요청됨.');
        res.send("true");
    });
    //false
    router.route('/false').get(function(req, res) {
        console.log('/false 패스 요청됨.');
        res.send("false");
    });

    // 로그인 인증
    router.route('/login').post(passport.authenticate('local-login', {
        successRedirect : '/true', 
        failureRedirect : '/false', 
        failureFlash : true 
    }));
    // 회원가입 인증
    router.route('/signup').post(passport.authenticate('local-signup', {
        successRedirect : '/true', 
        failureRedirect : '/false', 
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
