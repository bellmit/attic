import React from 'react'
import { mount } from 'enzyme'

import SquadEditor from 'app/squad/components/SquadEditor'

describe('SquadEditor', function() {
  var api = {
    id: 'testing api dummy',
  }

  describe('when loading', function() {
    var characters = []
    var workflow = {
      loading: true,
      saving: false,
    }

    beforeEach(function() {
      this.loadSquad = chai.spy()
      this.wrapper = mount(
        <SquadEditor.Component
          api={api}
          workflow={workflow}
          characters={characters}
          loadSquad={this.loadSquad} />
      )
    })

    it('shows a disabled loading button', function() {
      var saveButton = this.wrapper.find('.btn-save')
      expect(saveButton).to.have.text('Loading…')
      expect(saveButton).to.have.className('disabled')
      expect(saveButton).to.have.attr('disabled')
    })
  })

  describe('when quiescent', function() {
    var characters = [
      {
        name: 'Alice',
        archetype: 'SAGE',
        sprite: {
          hat: '0',
          hair: '1',
          outfit: '1',
          gender: 'F',
        },
      },
    ]
    var workflow = {
      loading: false,
      saving: false,
    }

    beforeEach(function() {
      this.loadSquad = chai.spy()
      this.wrapper = mount(
        <SquadEditor.Component
          api={api}
          workflow={workflow}
          characters={characters}
          loadSquad={this.loadSquad} />
      )
    })

    it('displays character editors for squads', function() {
      var characters = this.wrapper.find('.character-editor')
      expect(characters).to.have.length(1)
      expect(characters.at(0).find('.name-input input')).to.have.value('Alice')
    })

    it('shows an enabled save button', function() {
      var saveButton = this.wrapper.find('.btn-save')
      expect(saveButton).to.have.text('Save & return to lobby')
      expect(saveButton).not.to.have.className('disabled')
      expect(saveButton).not.to.have.attr('disabled')
    })
  })

  describe('when saving', function() {
    var characters = [
      {
        name: 'Alice',
        archetype: 'SAGE',
        sprite: {
          hat: '0',
          hair: '1',
          outfit: '1',
          gender: 'F',
        },
      },
    ]
    var workflow = {
      loading: false,
      saving: true,
    }

    beforeEach(function() {
      this.loadSquad = chai.spy()
      this.wrapper = mount(
        <SquadEditor.Component
          api={api}
          workflow={workflow}
          characters={characters}
          loadSquad={this.loadSquad} />
      )
    })

    it('displays character editors for squads', function() {
      var characters = this.wrapper.find('.character-editor')
      expect(characters).to.have.length(1)
      expect(characters.at(0).find('.name-input input')).to.have.value('Alice')
    })

    it('shows a disabled save button', function() {
      var saveButton = this.wrapper.find('.btn-save')
      expect(saveButton).to.have.text('Saving…')
      expect(saveButton).to.have.className('disabled')
      expect(saveButton).to.have.attr('disabled')
    })
  })
})
