import React from 'react'
import { mount } from 'enzyme'

import CharacterEditor from 'app/squad/components/CharacterEditor'

describe('CharacterEditor', function() {
  var name = 'Alice'
  var index = 1

  beforeEach(function() {
    var archetype = 'SKIRMISHER'
    var sprite = {
      hair: '1',
      hat: '0',
      outfit: '1',
      gender: 'F',
    }

    this.updateCharacterSprite = chai.spy()
    this.changeCharacterName = chai.spy()
    this.changeCharacterArchetype = chai.spy()
    this.wrapper = mount(
      <CharacterEditor
        index={index}
        archetype={archetype}
        name={name}
        sprite={sprite}
        updateCharacterSprite={this.updateCharacterSprite}
        changeCharacterName={this.changeCharacterName}
        changeCharacterArchetype={this.changeCharacterArchetype} />
    )
  })

  it('shows the selected sprite', function() {
    var spriteImage = this.wrapper.find('.character-sprite image').get(0)

    // "B", // "battle" sprites
    // SPRITE_WIDTH, "x", SPRITE_HEIGHT,
    // gender,
    // "1", // sprite group
    // "H", hair,
    // "-", hat,
    // "C", outfit,

    expect(spriteImage.getAttribute('xlink:href')).to.match(/^.*[/]B128x240F1H1-0C1[.][0-9a-f]{32}[.]png$/)
  })

  it('edits hair', function() {
    this.wrapper.find('.hairs-input .btn-next').simulate('click')
    expect(this.updateCharacterSprite).to.have.been.called.with(index, {
      hair: '2',
    })
  })

  it('edits hat', function() {
    this.wrapper.find('.hats-input .btn-next').simulate('click')
    expect(this.updateCharacterSprite).to.have.been.called.with(index, {
      hat: '1',
    })
  })

  it('edits outfit', function() {
    this.wrapper.find('.outfits-input .btn-next').simulate('click')
    expect(this.updateCharacterSprite).to.have.been.called.with(index, {
      outfit: '2',
    })
  })

  it('shows the selected gender', function() {
    var genderButtons = this.wrapper.find('.genders-input .btn-default')
    expect(genderButtons.at(0)).to.have.className('active')
    expect(genderButtons.at(1)).not.to.have.className('active')
  })

  it('edits gender', function() {
    this.wrapper.find('.genders-input .btn-default').at(1).simulate('click')
    expect(this.updateCharacterSprite).to.have.been.called.with(index, {
      gender: 'M',
    })
  })

  it('shows the name', function() {
    expect(this.wrapper.find('.name-input input')).to.have.value('Alice')
  })

  it('edits the name', function() {
    var nameInput = this.wrapper.find('.name-input input')
    nameInput.get(0).value = 'Bob'
    nameInput.simulate('change')

    expect(this.changeCharacterName).to.have.been.called.with(index, 'Bob')
  })

  it('has three archetype buttons', function() {
    var archetypeButtons = this.wrapper.find('.archetype-input .btn-default')
    expect(archetypeButtons).to.have.length(3)
  })

  it('shows the selected archetype', function() {
    var archetypeButtons = this.wrapper.find('.archetype-input .btn-default')
    expect(archetypeButtons.at(0)).to.have.className('active')
    expect(archetypeButtons.at(1)).not.to.have.className('active')
    expect(archetypeButtons.at(2)).not.to.have.className('active')
  })

  it('edits the archetype', function() {
    this.wrapper.find('.archetype-input .btn-default').at(1).simulate('click')
    expect(this.changeCharacterArchetype).to.have.been.called.with(index, 'HUNTER')
  })
})
