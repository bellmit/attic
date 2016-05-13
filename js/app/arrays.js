'use strict';

/*
 * Given an index and an array, map the index to an array slot. This assumes
 * the array is infinitely tiled in both directions: the index -1 will be
 * mapped to the last index in the array, while the index (length + 1) will be
 * mapped to the first.
 */
export function cyclicIndex(index, array) {
  while (index < 0) {
    index += array.length;
  }
  while (index >= array.length) {
    index -= array.length;
  }
  return index;
};

/* Given an array, return a random index. */
export function randomIndex(array) {
  return Math.floor(Math.random() * array.length);
}

/* Given an array, return a random element. */
export function randomElement(array) {
  return array[randomIndex(array)];
}