//reservation schema

var crypto = require('crypto');

var Schema = {};

Schema.createSchema = function(mongoose) {
	//스키마 정의
	var ReservationSchema = mongoose.Schema({
        item_id : {type:Number}
		, owner_email : {type:String,'default':''}
		, brower_email : {type:String,'default':''}
        , start_date : {type:Date,index: {unique: false},'default':Date.now}
        , end_date : {type:Date,index: {unique: false},'default':Date.now}
        , end_to : {type:String}
    });
	return ReservationSchema;
};

// module.exports에 ReservationSchema 객체 직접 할당
module.exports = Schema;

