const get2 = (number, done) => {
    setTimeout(() => {
        done(number * number)
    }, 2000)
}
const get2Normal = (number) => number * number;

const get2p = (number) => {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            if (number > 10)
                reject("number > 10");
            else
                resolve(number * 3);
        }, 2000)
    });
}

const get5 = (number, done) => {
    setTimeout(() => {
        done(number * number * number * number * number)
    }, 2000)
}

const get5p = (number) => {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            if (number > 20)
                reject("number > 20");
            else
                resolve(number * number * number * number * number)
        }, 2000)
    });
}

const get7 = (number, done) => {
    setTimeout(() => {
        done(number * number * 0.7)
    }, 7000)
}

const get7p = (number) => {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            resolve(number * number * 0.7)
        }, 7000)
    });
}

let number = 5;
// get2(5, (result1) => {
//     get5(result1, (result2) => {
//         get7(result2, (result3) => {
//             console.log(result3)
//         })
//     })
// })


const run = async(number) => {
    //可以這樣抓exception
    let result = await get2p(number).catch((error) => {
        console.log(error);
    });
    console.log(result);
    //也可以這樣抓error
    try {
        result = await get5p(number);
    } catch (error) {
        console.log(error);
    }
    console.log(result);
}

//run2會變成promise
const run2 = async (number) => {
    let result = await get2p(number).catch((error) => {
        console.log(error);
    });
    console.log(result);
    //也可以這樣抓error
    try {
        result = await get5p(number);
    } catch (error) {
        console.log(error);
    }
    return result;
}
const run1 = async(number) => {
    let result = await run2(number)
    console.log("run1:", result);
    
}
// run(3)
run1(3);