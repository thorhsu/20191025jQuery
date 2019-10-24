// const hello = () => {
//         console.log('hello');
//     }
    // setTimeout(hello, 10000)

const hello2 = (done) => {
    setTimeout(() => {
        done('hello world')
    }, 2000);
}

hello2(result => console.log(result));