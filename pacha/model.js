const _ = require('lodash')
const Sequelize = require('sequelize')
const bigInt = require('big-integer')

const Op = Sequelize.Op

const sequelize = new Sequelize(process.env.DATABASE_URL, {
    operatorsAliases: false,
    logging: false,
})

const Frame = sequelize.define('frame', {
    current_frame: {
        type: Sequelize.BIGINT,
        primaryKey: true,
    },
}, {
    tableName: 'frame',
    timestamps: false,
})

const Command = sequelize.define('command', {
    frame: {
        type: Sequelize.BIGINT,
        primaryKey: true,
    },
    seq: {
        type: Sequelize.BIGINT,
        primaryKey: true,
    },
    command: Sequelize.JSON,
}, {
    tableName: 'command',
    timestamps: false,
})

const State = sequelize.define('state', {
    frame: {
        type: Sequelize.BIGINT,
        primaryKey: true,
    },
    state: Sequelize.JSON,
}, {
    tableName: 'state',
    timestamps: false,
})

const Events = sequelize.define('events', {
    frame: {
        type: Sequelize.BIGINT,
        primaryKey: true,
    },
    events: Sequelize.JSON,
}, {
    tableName: 'events',
    timestamps: false,
})

module.exports = {
    async transaction(cb) {
        return await sequelize.transaction(cb)
    },

    async addCommand(t, frame, command) {
        return await Command.create({
            frame: frame.toString(),
            command,
        }, {
            transaction: t,
        })
    },

    async currentFrame(t) {
        const frame = await Frame.findOne({
            transaction: t,
        })
        return bigInt(frame.current_frame)
    },

    async oldestState(t) {
        const state = await State.findOne({
            order: [
                ['frame', 'ASC'],
            ],
            transaction: t,
        })
        return [bigInt(state.frame), state.state]
    },

    async advanceFrame(t) {
        const prev = await Frame.findOne({
            transaction: t,
        })
        const [[[next]]] = await Frame.increment(['current_frame'], {
            where: {
                current_frame: prev.current_frame,
            },
            transaction: t,
        })
        if (!next)
            return []
        return [bigInt(prev.current_frame), bigInt(next.current_frame)]
    },

    async deleteBefore(t, frame) {
        await Command.destroy({
            where: {
                frame: {[Op.lt]: frame.toString()},
            },
            transaction: t,
        })
        await Events.destroy({
            where: {
                frame: {[Op.lt]: frame.toString()},
            },
            transaction: t,
        })
        await State.destroy({
            where: {
                frame: {[Op.lt]: frame.toString()},
            },
            transaction: t,
        })
    },

    async frameCommands(t, frame) {
        const commands = await Command.findAll({
            where: {
                frame: frame.toString(),
            },
            order: [
                ['seq', 'ASC'],
            ],
            transaction: t,
        })
        return commands.map(c => c.command)
    },

    async lastStateAt(t, frame) {
        const state = await State.findOne({
            where: {
                frame: {[Op.lte]: frame.toString()},
            },
            order: [
                ['frame', 'DESC'],
            ],
            transaction: t,
        })
        if (!state)
            return []
        return [bigInt(state.frame), state.state]
    },

    async frameEvents(t, frame) {
        const events = await Events.findOne({
            where: {
                frame: frame.toString(),
            },
            transaction: t,
        })
        if (!events)
            return []
        return events.events
    },

    async frameRangeEvents(t, afterFrame, beforeFrame) {
        const events = await Events.findAll({
            where: {
                [Op.and]: [
                    {frame: {[Op.gt]: afterFrame.toString()}},
                    {frame: {[Op.lte]: beforeFrame.toString()}},
                ],
            },
            order: [
                ['frame', 'ASC'],
            ],
            transaction: t,
        })
        return _.flatMap(events, event => event.events)
    },

    async addEvents(t, frame, events) {
        await Events.create({
            frame: frame.toString(),
            events,
        }, {
            transaction: t,
        })
    },

    async addState(t, frame, state) {
        return await State.create({
            frame: frame.toString(),
            state,
        }, {
            transaction: t,
        })
    },
}
