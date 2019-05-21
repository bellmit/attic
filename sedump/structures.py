import collections as c

# Creates a class mapped to XML. The name is a simple string and will be used as
# the class name. The attribute mappings are a list of pairs of name and
# accessor function.
#
# An element class exposes the following conventions:
#
# * A read-only property for each attribute in attribute_mappings,
# * A from_element class method that accepts an lxml.etree element and extracts
#   an instance of the class, and
# * An embedded_at class method that produces an accessor that extracts the
#   class from a containing element, by XPath selector, to allow elements to be
#   nested together.
# * A collection_at class method that produces an accessor that extracts
#   sequences of the class from a containing element, by XPath selector. Each
#   match in the document will produce an independent instance.
#
# The resulting class can be subclassed to add additional behaviour, if desired.
def element_class(name, attribute_mappings):
    structure = c.namedtuple(name, [attribute for (attribute, _) in attribute_mappings])

    @classmethod
    def from_element(cls, elem):
        values = {
            name: accessor(elem)
            for (name, accessor) in attribute_mappings
        }
        return cls(**values)

    @classmethod
    def embedded_at(cls, selector):
        return lambda elem: cls.from_element(*elem.xpath(selector))

    @classmethod
    def collection_at(cls, selector, sort_key=None):
        accessor = lambda elem: [
            cls.from_element(element)
            for element in elem.xpath(selector)
        ]
        if sort_key is not None:
            return lambda elem: sorted(accessor(elem), key=sort_key)
        return accessor

    return type(name, (structure,), dict(
        from_element=from_element,
        embedded_at=embedded_at,
        collection_at=collection_at,
    ))

# Returns an accessor function that retrieves the given XPath selector from the
# target element. Useful for extracting properties whose values are simple XPath
# expressions.
def xpath(selector):
    return lambda elem: elem.xpath(selector)

# Creates a class that reads whole collections of mapped classes from an XML
# document.
#
# The resulting class exposes the following behaviours:
#
# * It can be constructed from a list of objects of ``of_type``,
# * It can be iterated over to recover the elements of that list, sorted by
#   sort_key,
# * It has a from_document class method that accepts an lxml.etree object and
#   extracts a collection of of_type objects as found by selector, using
#   of_type's from_element method, and
# * It has a from_context method that reads the whole mess from `path` in the
#   passed context.
# * It has a with_id instance method that will quickly find a given entry by ID.
#
# As with element_class classes, these classes can be subtyped if you want to
# add behaviours.
def document_class(name, of_type, sort_key, path, selector):
    def __init__(self, entries):
        self.entries = sorted(entries, key=sort_key)
        self.entries_by_id = {
            entry.id: entry
            for entry in entries
        }

    def __iter__(self):
        return iter(self.entries)

    def with_id(self, id):
        return self.entries_by_id[id]

    @classmethod
    def from_context(cls, context):
        components = context.xml(path)
        return cls.from_document(components)

    @classmethod
    def from_document(cls, document):
        entries = document.xpath(selector)
        return cls([of_type.from_element(entry) for entry in entries])

    return type(name, (object,), dict(
        __init__=__init__,
        __iter__=__iter__,
        with_id=with_id,
        from_context=from_context,
        from_document=from_document,
    ))

# A handy sort key for things that expose a sortable ID
def by_id(obj):
    return obj.id

# Space Engineers structures

class Id(element_class('Id', [
    ('type_id', xpath('string(TypeId)')),
    ('subtype_id', xpath('string(SubtypeId)')),
])):
    @classmethod
    def attributes_at(cls, type_id_selector, subtype_id_selector):
        return lambda elem: cls(type_id=elem.xpath(type_id_selector), subtype_id=elem.xpath(subtype_id_selector))

Component = element_class('Component', [
    ('id', Id.embedded_at('Id')),
    ('name', xpath('string(DisplayName)')),
    ('mass', xpath('number(Mass)')),
    ('volume', xpath('number(Volume)')),
    ('integrity', xpath('number(MaxIntegrity)')),
])

Components = document_class('Components', Component, by_id, 'Components.sbc', '/Definitions/Components/Component')

PhysicalItem = element_class('PhysicalItem', [
    ('id', Id.embedded_at('Id')),
    ('name', xpath('string(DisplayName)')),
    ('mass', xpath('number(Mass)')),
    ('volume', xpath('number(Volume)')),
])

PhysicalItems = document_class('PhysicalItems', PhysicalItem, by_id, 'PhysicalItems.sbc', '/Definitions/PhysicalItems/PhysicalItem')

BlueprintItem = element_class('BlueprintItem', [
    ('amount', xpath('number(@Amount)')),
    ('item_id', Id.attributes_at('string(@TypeId)', 'string(@SubtypeId)'))
])

def blueprint_results(elem):
    results = elem.xpath('Result')
    if len(results) == 0:
        results = elem.xpath('Results/Item')
    return [BlueprintItem.from_element(result) for result in results]

Blueprint = element_class('Blueprint', [
    ('id', Id.embedded_at('Id')),
    ('results', blueprint_results),
    ('prerequisites', BlueprintItem.collection_at('Prerequisites/Item', lambda item: item.item_id)),
    ('base_time', xpath('number(BaseProductionTimeInSeconds)'))
])

class Blueprints(document_class('Blueprints', Blueprint, by_id, 'Blueprints.sbc', '/Definitions/Blueprints/Blueprint')):
    def __init__(self, entries):
        super().__init__(entries)

        self.entries_by_result = c.defaultdict(list)
        for entry in self.entries:
            for result in entry.results:
                self.entries_by_result[result.item_id].append(entry)

    def for_product(self, item_id):
        return self.entries_by_result[item_id]

BlockComponent = element_class('BlockComponent', [
    ('subtype_id', xpath('string(@Subtype)')),
    ('quantity', xpath('number(@Count)')),
])
class BlockComponent(BlockComponent):
    @property
    def item_id(self):
        return Id(type_id='Component', subtype_id=self.subtype_id)

Block = element_class('Block', [
    ('id', Id.embedded_at('Id')),
    ('name', xpath('string(DisplayName)')),
    ('pcu', xpath('number(PCU)')),
    ('size', xpath('string(CubeSize)')),
    ('width', xpath('number(Size/@x)')),
    ('height', xpath('number(Size/@y)')),
    ('depth', xpath('number(Size/@z)')),
    ('components', BlockComponent.collection_at('Components/Component')),
])

Blocks = document_class('Blocks', Block, by_id, 'CubeBlocks.sbc', '/Definitions/CubeBlocks/Definition')
