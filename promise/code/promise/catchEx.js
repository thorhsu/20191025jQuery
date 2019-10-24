const p1 = new Promise((resolve, reject) => {
    throw new Error('Exception before');
    resolve(4);
});

p1.then((val) => {
        console.log(val) //4
        return val + 2
    }).catch((error) => {
        console.log("first catch:", error.message);
        // throw new Error('The first error!')
    }).then((val) => {
        console.log(val) //6
        throw new Error('The second error!')
    }).catch((err) => {      
        //catch無法抓到上個promise的回傳值
        console.log("The second catch:", err.message)
        //這裡如果有回傳值，下一個then可以抓得到
        //return 100
    }).then((val) => console.log(val, 'done')) //val是undefined，回傳值消息