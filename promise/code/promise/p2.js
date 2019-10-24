const hello2_old = (done) => {
    setTimeout(() => {
        done('hello world')
    }, 2000)
}

const hello2 = () => {
    return new Promise(function(resolve, reject) {
        setTimeout(() => {
            resolve('hello2')
        }, 2000)
    })
}

hello2().then(function(result) {
    console.log(result)
})