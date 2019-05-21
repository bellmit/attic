import jinja2 as j

def environment(gamedata):
    environment = j.Environment(
        loader=j.FileSystemLoader('pages'),
        autoescape=j.select_autoescape(['html', 'xml']),
    )
    environment.globals['gamedata'] = gamedata
    environment.filters['component'] = gamedata.components.with_id
    environment.filters['localize'] = gamedata.localization.localize
    environment.filters['block_mass'] = lambda block: block_mass(block, gamedata)
    return environment

def block_mass(block, gamedata):
    def component(slot):
        return gamedata.components.with_id(slot.item_id)

    return sum(slot.quantity * component(slot).mass for slot in block.components)
