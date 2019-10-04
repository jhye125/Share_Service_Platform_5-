
module.exports = function(router, passport) {
    console.log('user_passport 호출됨.');

    // 로그아웃
    router.route('/logout').get(function(req, res) {
        console.log('/logout 패스 요청됨.');
        // 로그아웃 하고 db에 user data 를 업데이트 하는 과정 필요. 추가해야함.
        req.logout();
    });

    router.route('/login_success').get(function(req,res){
        console.log('/login 성공');
        res.send('true');
    });
    router.route('/login_failure').get(function(req,res){
        console.log('/login 실패');
        res.send('false');
    });
    // 로그인 인증
    router.route('/login').post(passport.authenticate('local-login', {
        successRedirect : '/login_success', 
        failureRedirect : '/login_failure', 
        failureFlash : true 
    }));
    router.route('/signup_success').get(function(req,res){
        console.log('/signup 성공');
        res.send('true');
    });
    router.route('/signup_failure').get(function(req,res){
        console.log('/signup 실패');
        res.send('false');
    });
    // 회원가입 인증
    router.route('/signup').post(passport.authenticate('local-signup', {
        successRedirect : '/signup_success', 
        failureRedirect : '/signup_failure', 
        failureFlash : true 
    }));
    // 패스포트 - 구글 인증 라우팅 
    router.route('/auth/google').get(passport.authenticate('google', { 
        scope : 'u_id' 
    }));

    // 패스포트 - 구글 인증 콜백 라우팅
    router.route('/auth/google/callback').get(passport.authenticate('facebook', {
        successRedirect : '/profile',
        failureRedirect : '/'
    }));

};