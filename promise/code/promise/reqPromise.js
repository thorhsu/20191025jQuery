const request = require('request-promise');
let addressUrl = `https://maps.googleapis.com/maps/api/geocode/json?key=${key}&address=Taipei&callback=initMap`;

request(addressUrl).then((response) => {
    console.log(response);
    let result = JSON.parse(response);
    let lat = result.results[0].geometry.location.lat;
    let lng = result.results[0].geometry.location.lng;
    let placeAPI = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json?'+
    `key=${key}&location=${lat},${lng}&radius=500&type=restaurant&key=${key}`;
    request(placeAPI, function (error, response, body) {
          console.log(body)
    });
}).catch((response) => console.log("error", response));

