let nodemailer = require('nodemailer');

let from = "youremail@domain.com"; //change accordingly 
let password="yourAES256CipherPassword";
let to="youremail@domain.com";
let filePath = __dirname+"/myreport2.json";

let mail = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: from,
    pass: password
  }
});

let mailOptions = {
  from: from,
  to: to,
  subject: 'Artillery results',
  text: 'Artillery tests results anexed!',
  attachments: [
        {   // file on disk as an attachment
            filename: 'myreport2.json',
            path:filePath // stream this file
        }
        ]
}

mail.sendMail(mailOptions, function(error, info){
  if (error) {
    console.log(error);
  } else {
    console.log('Email sent: ' + info.response);
  }
});
let hello = "daniel";

console.log(hello);
console.log(__dirname);
