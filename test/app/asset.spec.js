'use strict'

import asset from 'app/asset'

describe("asset", () => {
  describe("non-Aerobatic environments", () => {
    before(() => {
      delete window.appConfig
    })

    it("returns paths unchanged", () => {
      var result = asset("/test/path")
      expect(result).to.equal("/test/path")
    })
  })

  describe("Aerobatic environments", () => {
    before(() => {
      window.appConfig = {
        STATIC_ASSET_PATH: "https://assets.example.com",
      }
    })

    it("returns paths with asset path prepended", () => {
      var result = asset("/test/path")
      expect(result).to.equal("https://assets.example.com/test/path")
    })
  })

  describe("Incomplete Aerobatic environments", () => {
    before(() => {
      window.appConfig = {
        /* no STATIC_ASSET_PATH */
      }
    })

    it("returns paths with asset path prepended", () => {
      var result = asset("/test/path")
      expect(result).to.equal("/test/path")
    })
  })

  var savedAppConfig = undefined

  before(() => {
    savedAppConfig = window.appConfig || undefined
  })

  after(() => {
    if (savedAppConfig === undefined) {
      delete window.appConfig
    } else {
      window.appConfig = savedAppConfig
    }
  })
})

