const request = require('request');
let key = 'AIzaSyCZhfps2a25Z-aZWonu29Kkilm8b3tvlcQ';
let addressUrl = `https://maps.googleapis.com/maps/api/geocode/json?key=${key}&address=Taipei&callback=initMap`;


request(addressUrl, function (error, response, body) {
    let responseBody = JSON.parse(body);
    let lat = responseBody.results[0].geometry.location.lat;
    let lng = responseBody.results[0].geometry.location.lng;
    let placeAPI = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json?'+
    `key=${key}&location=${lat},${lng}&radius=500&type=restaurant&key=AIzaSyBImOy7k7q3nRG0YOcN2Z4GfQDu3q7WYNE`;
    request(placeAPI, function (error, response, body) {
            console.log('Restaruants around')
          console.log(body)
    });

});
