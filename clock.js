function tick() {
  console.log('tick')
  schedule()
}

function schedule() {
  setTimeout(tick, 1000)
}

schedule()
