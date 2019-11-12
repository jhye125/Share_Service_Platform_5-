
/*
 * ?¤ì •
 */

module.exports = {
	server_port: 3000,
	db_url: 'mongodb://localhost:27017/local',
	db_schemas: [
		{file:'./user_schema', collection:'users', schemaName:'UserSchema', modelName:'UserModel'},
		{file:'./item_schema',collection:'items',schemaName:'ItemSchema',modelName:'ItemModel'},
    {file:'./reservation_schema',collection:'reservations',schemaName:'ReservationSchema',modelName:'ReservationModel'}
	],
	route_info: [
	],
	google: {		// passport google
		clientID: 'id',
		clientSecret: 'secret',
		callbackURL: '/auth/google/callback'
	}
}
