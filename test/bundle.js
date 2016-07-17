import chaiEnzyme from 'chai-enzyme'
import spies from 'chai-spies'

chai.use(chaiEnzyme())
chai.use(spies)

var context = require.context('.', true, /.+\.spec\.js$/)
context.keys().forEach(context)
module.exports = context
