
module.exports = function(router,fs) {
    console.log('setimage 호출됨.');

    router.route('/set_image').post(function(req,res){
        console.log('/set_image 요청');
        var name = req.body.file_name;
        var data = req.body.data;
        //console.log(file_contents);
        //console.log(typeof(file_contents));
        var path = "./images/";
        path=path.concat(name);
        console.log(path);

        fs.open(path,'w',function(err,fd){ 
            if (err){
                throw err; 
            }
            console.log('file open complete');
            fs.writeFile(path,data,function(err){
                if(err)
                    throw err;
                console.log('write end');
            });
        });
    });

};
