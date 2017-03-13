db.users.remove( { } )
db.users.insert({name:"user", password:"5f4dcc3b5aa765d61d8327deb882cf99", email:"", role:"ADMIN,USER"});
db.users.insert({name:"dbhise", password:"81dc9bdb52d04dc20036dbd8313ed055", email:"", role:"ADMIN,USER"});
db.users.createIndex( { "name": 1 }, { unique: true } )
