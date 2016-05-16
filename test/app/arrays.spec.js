'use strict';

import * as arrays from 'app/arrays';

describe('arrays', () => {
  var array = ['one', 'two', 'three'];

  describe('.cyclicIndex', () => {
    it('returns indices in the array bounds unchanged', () => {
      expect(arrays.cyclicIndex(0, array)).to.equal(0);
      expect(arrays.cyclicIndex(1, array)).to.equal(1);
      expect(arrays.cyclicIndex(2, array)).to.equal(2);
    });

    it('cycles indices past the end of the array', () => {
      expect(arrays.cyclicIndex(3, array)).to.equal(0);
      expect(arrays.cyclicIndex(4, array)).to.equal(1);
      expect(arrays.cyclicIndex(5, array)).to.equal(2);
      expect(arrays.cyclicIndex(6, array)).to.equal(0);
    });

    it('cycles indices prior to the start of the array', () => {
      expect(arrays.cyclicIndex(-4, array)).to.equal(2);
      expect(arrays.cyclicIndex(-3, array)).to.equal(0);
      expect(arrays.cyclicIndex(-2, array)).to.equal(1);
      expect(arrays.cyclicIndex(-1, array)).to.equal(2);
    });
  });

  describe('.randomIndex', () => {
    it('returns a valid index', () => {
      // You might be tempted to refactor this to a single test using .within.
      // Don't. .within's bounds are inclusive, and an array is smaller than
      // that.
      expect(arrays.randomIndex(array)).to.be.at.least(0);
      expect(arrays.randomIndex(array)).to.be.lessThan(array.length);
    });
  });

  describe('.randomElement', () => {
    it('returns an array element', () => {
      expect(arrays.randomElement(array)).to.be.oneOf(array);
    });
  });
});