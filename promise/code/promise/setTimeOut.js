for(var i = 0 ; i <= 5 ; i++){
    console.log(b);
    setTimeout(function(){
        console.log(i);
    }, i * 1000);
    var b = 3;
}
// console.log("outside:" + i);

//閉包
for(var i = 1 ; i <= 5; i++){
    (function(i){
        setTimeout(function(){
            console.log(i);
        }, i * 1000)
    })(i);
}
//setTimeout引數
for(var i = 1 ; i <= 5; i++){
    setTimeout(function(i){
        console.log("argument:", i);
    }, i * 1000, i);
}
//bind
for(var i = 1 ; i <= 5; i++){
    setTimeout(function(i){
        console.log("bind:", this.valueOf());
    }.bind(i), i * 1000)
   
}