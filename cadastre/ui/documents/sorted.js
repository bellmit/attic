// A comparator appropriate for strings (astonishingly, I didn't find one in
// MDN)
export function stringCompare(a, b) {
    if (a < b)
        return -1
    if (a > b)
        return 1
    return 0
}

function reverseComparator(f) {
    return (a, b) => -f(a, b)
}

// Returns a sorted view of the passed list, based on a set of options:
//
// * `comparators` is a table of comparator functions.
// * `sort` is the name of the comparator function to use.
// * `reverse` reverses the normal sense of the selected comparator.
//
// This odd argument convention is designed to be easier to call inside of a
// state reducer.
export default function sorted(values, comparators, sort, reverse) {
    if (!values)
        return values
    const baseComparator = comparators[sort]
    const comparator = reverse ?
        reverseComparator(baseComparator) :
        baseComparator
    return sort ?
        values.concat().sort(comparator) :
        values
}
