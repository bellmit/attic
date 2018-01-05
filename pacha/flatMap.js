module.exports = function flatMap(array, f) {
    return Array.prototype.concat.apply([], array.map(f))
}
