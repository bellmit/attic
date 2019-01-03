import chaiEnzyme from 'chai-enzyme'

import { App } from 'components'

chai.use(chaiEnzyme())

var context = require.context('.', true, /.+\.spec\.jsx?$/)
context.keys().forEach(context)
module.exports = context
