const express = require('express')
const path = require('path')

const router = module.exports = express.Router()

router.use('/bundle', express.static(path.resolve('static/bundle'), { maxAge: '1 year' }))
router.get('/', (req, res) => res.sendFile(path.resolve('static/index.html')))
