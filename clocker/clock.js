function tick() {
  console.log('tick tock')
  schedule()
}

function schedule() {
  setTimeout(tick, 1000)
}

schedule()
