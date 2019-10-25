const request = require('request');
// let key = 'AIzaSyCZhfps2a25Z-aZWonu29Kkilm8b3tvlcQ';
let key = 'AIzaSyCZhfps2a25Z-aZWonu29K';
let addressUrl = `https://maps.googleapis.com/maps/api/geocode/json?key=${key}&address=Taipei&callback=initMap`;



let promise = new Promise((resolve, reject) => {
  request(addressUrl, function (error, response, body) {
    let result = JSON.parse(body);
    if (error || result.status !== 'OK') {
        // reject(result);
        return reject(new Error('error occured'))
    } else {
        resolve(result);
    }
  })
})

promise.then().catch();


promise.then(function (result) {   
    let lat = result.results[0].geometry.location.lat;
    let lng = result.results[0].geometry.location.lng;
    let placeAPI = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json?'+
    `key=${key}&location=${lat},${lng}&radius=500&type=restaurant&key=AIzaSyBImOy7k7q3nRG0YOcN2Z4GfQDu3q7WYNE`;
    request(placeAPI, function (error, response, body) {
          console.log('place')
          console.log(body)
    });
}, function (error) {
    console.log('on reject');
    console.log(error.message);

});

promise.then(function (result) {   
    let lat = result.results[0].geometry.location.lat;
    let lng = result.results[0].geometry.location.lng;
    let placeAPI = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json?'+
    `key=${key}&location=${lat},${lng}&radius=500&type=restaurant&key=AIzaSyBImOy7k7q3nRG0YOcN2Z4GfQDu3q7WYNE`;
    request(placeAPI, function (error, response, body) {
          console.log('place')
          console.log(body)
    });
}).catch(function(error){
    console.log('on catch');
    console.log(error.message);
});

