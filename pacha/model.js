const Sequelize = require('sequelize')

const Op = Sequelize.Op

const sequelize = new Sequelize(process.env.DATABASE_URL, {
    operatorsAliases: false,
    logging: false,
})

const Frame = sequelize.define('frame', {
    oldest_frame: {
        type: Sequelize.BIGINT,
        primaryKey: true,
    },
    current_frame: {
        type: Sequelize.BIGINT,
        primaryKey: true,
    },
    command_frame: {
        type: Sequelize.BIGINT,
        primaryKey: true,
    }
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
            frame,
            command,
        }, {
            transaction: t,
        })
    },

    async frame(t) {
        return await Frame.findOne({
            transaction: t,
        })
    },

    async advanceFrame(t) {
        const prev = await Frame.findOne({
            transaction: t,
        })
        const [[[next]]] = await Frame.increment(['oldest_frame', 'current_frame', 'command_frame'], {
            where: {
                current_frame: prev.current_frame,
            },
            transaction: t,
        })
        if (!next)
            return []
        await this.pruneFrame(t, next.oldest_frame)
        return [next.current_frame, prev.current_frame]
    },

    async pruneFrame(t, frame) {
        await Promise.all([
            State.destroy({
                where: {
                    frame: {[Op.lt]: frame}
                },
                transaction: t,
            }),
            Command.destroy({
                where: {
                    frame: {[Op.lt]: frame}
                },
                transaction: t,
            }),
            Events.destroy({
                where: {
                    frame: {[Op.lt]: frame}
                },
                transaction: t,
            }),
        ])
    },

    async frameCommands(t, frame) {
        const commands = await Command.findAll({
            where: {
                frame,
            },
            order: [
                ['seq', 'ASC'],
            ],
            transaction: t,
        })
        return commands.map(c => c.command)
    },

    async frameState(t, frame) {
        const state = await State.findOne({
            where: {
                frame,
            },
            transaction: t,
        })
        if (!state)
            return undefined
        return state.state
    },

    async frameEvents(t, frame) {
        const events = await Events.findOne({
            where: {
                frame,
            },
            transaction: t,
        })
        if (!events)
            return []
        return events.events
    },

    async addEvents(t, frame, events) {
        await Events.create({
            frame,
            events,
        }, {
            transaction: t,
        })
    },

    async addState(t, frame, state) {
        return await State.create({
            frame,
            state,
        }, {
            transaction: t,
        })
    },
}
