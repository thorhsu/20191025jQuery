const request = require('request');
console.log(1)
request('http://www.google.com', 
function (error, response, body) {
  console.log('error:', error);
  console.log('statusCode:', response.statusCode);
  console.log(2)
})
console.log(3)