import json

from alcs_funcs import *
from mcresources.resource_manager import ResourceManager, utils
from itertools import repeat

rm = ResourceManager('bsa', 'src/main/resources', 2, False, 'en_us')
tfc_rm = ResourceManager('tfc', 'src/main/resources', 2, False, 'en_us')
forge_rm = ResourceManager('forge', 'src/main/resources', 2, False, 'en_us')
STONE_TOOL_HEADS = ('hammer', 'hoe', 'javelin', 'knife', 'shovel', 'axe')
STONE_TOOL_BINDINGS = ('weak', 'medium', 'strong')
BINDING_BONUSES = {'weak': 0, 'medium': 2, 'strong': 4}
ROCK_CATEGORY_DURABILITIES = {'igneous_extrusive': 70, 'igneous_intrusive': 60, 'metamorphic': 55, 'sedimentary': 50, 'flint': 120}
NON_BROKEN_GRAINS = ('barley', 'oat', 'rye', 'wheat')
BROKEN_GRAINS = ('maize', 'rice')
SHERD_PATTERNS = ('angler', 'archer', 'arms_up', 'blade', 'blank', 'brewer', 'burn', 'danger', 'explorer', 'friend',
                  'heart', 'heartbreak', 'howl', 'miner', 'mourner', 'plenty', 'prize', 'sheaf', 'shelter', 'skull',
                  'snort')



with open('templates/pot_model.json', 'r') as f:
    pot_template = f.read()
with open('templates/flat_block_model.json', 'r') as f:
    flat_block_template = f.read()


def bone_knapping(rm: ResourceManager, name_parts: ResourceIdentifier, pattern: List[str], result: ResourceIdentifier, ingredient: str = None, outside_slot_required: bool = False):
    knapping_recipe(rm, name_parts, 'bsa:bone', pattern, result, ingredient, outside_slot_required)

def loot_modifier_add_itemstack(rm: ResourceManager, loot_modifiers: list, name_parts, entity_tag, item, count):
    
    
    data = {
      "type": "bsa:add_itemstack",
      "conditions": [
        {
          "condition": "minecraft:entity_properties",
          "predicate": {
              "type": entity_tag
          },
          "entity": "this"
        }
      ],
      "item": item,
      "count": count
    }
    
    loot_modifier(rm, loot_modifiers, name_parts, data)

def loot_modifier_add_itemstack_min_max(rm: ResourceManager, loot_modifiers: list, name_parts, entity_tag, item, little, big):
    data = {
      "type": "bsa:add_itemstack_min_max",
      "conditions": [
        {
          "condition": "minecraft:entity_properties",
          "predicate": {
              "type": entity_tag
          },
          "entity": "this"
        }
      ],
      "item": item,
      "min": little,
      "max": big
    }
    
    loot_modifier(rm, loot_modifiers, name_parts, data)

    
def loot_modifier(rm: ResourceManager, loot_modifiers: list, name_parts, data):

    if isinstance(name_parts, str):
        name_parts = [name_parts]
    
    rm.data(['loot_modifiers'] + name_parts, data)
    
    loot_modifiers.append(f'bsa:{"/".join(name_parts)}')

def must_be_empty(ingredient: Json) -> Json:
    return {
        'type': 'bsa:must_be_empty',
        'ingredient': utils.ingredient(ingredient)
    }

def read_data_from_template(rm: ResourceManager, name_parts, template: str):
    rm.write(name_parts, json.loads(template))

def create_block_models():
    print("Creating block models...")
    for color in COLORS:
        read_data_from_template(rm, ('src', 'main', 'resources', 'assets', 'bsa', 'models', 'block', 'firepit_pot', color), pot_template % (color, color, color))
        
        rm.blockstate_multipart(f'ceramic/pot/{color}',
                ({'axis': 'x'}, {'model': f'bsa:block/firepit_pot/{color}'}),
                ({'axis': 'z'}, {'model': f'bsa:block/firepit_pot/{color}', 'y': 90}),
                ({'lit': True, 'axis': 'x'}, {'model': 'tfc:block/firepit_lit_low'}),
                ({'lit': True, 'axis': 'z'}, {'model': 'tfc:block/firepit_lit_low', 'y': 90}),
                ({'lit': False, 'axis': 'x'}, {'model': 'tfc:block/firepit_unlit'}),
                ({'lit': False, 'axis': 'z'}, {'model': 'tfc:block/firepit_unlit', 'y': 90})
            ).with_lang(lang(f'{color} Glazed Pot')).with_block_loot('tfc:powder/wood_ash', f'bsa:ceramic/pot/glazed/{color}')
        rm.item_model('pot', 'tfc:item/firepit_pot')
        
    read_data_from_template(rm, ('src', 'main', 'resources', 'assets', 'bsa', 'models', 'block', 'drying_sinew'), flat_block_template % ('bsa:block/drying_sinew', 'bsa:block/dried_sinew'))
    read_data_from_template(rm, ('src', 'main', 'resources', 'assets', 'bsa', 'models', 'block', 'dried_sinew'), flat_block_template % ('bsa:block/dried_sinew', 'bsa:block/dried_sinew'))
    
    rm.blockstate_multipart('sinew',
        ({'dried': False}, four_ways('bsa:block/drying_sinew')),
        ({'dried': True}, four_ways('bsa:block/dried_sinew'))
    )
    block = rm.block(('hide'))
    make_door(block)

def create_item_foods():
    print('Creating item foods...')
    for grain in GRAINS:
        food_item(rm, f'coarse_{grain}_flour', f'bsa:food/coarse_{grain}_flour', Category.grain, 4, 0, 0, 0.5, grain=0.3)
        food_item(rm, f'{grain}_flour', f'tfc:food/{grain}_flour', Category.grain, 4, 0, 0, 0.5, grain=0.4)
    for grain in NON_BROKEN_GRAINS:
        food_item(rm, f'crushed_{grain}_grain', f'bsa:food/crushed_{grain}_grain', Category.grain, 4, 0.25, 0, 0.375, grain=0.2)
    dynamic_food_item(rm, 'porridge', 'bsa:food/porridge', 'dynamic_bowl')

def create_item_heats():
    print('Creating item heat data...')
    item_heat(rm, ('ceramic', 'unfired_ceramic_jugs'), '#bsa:ceramic/unfired_jugs', 0.8)
    item_heat(rm, ('ceramic', 'unfired_ceramic_pots'), '#bsa:ceramic/unfired_pots', 0.8)
    item_heat(rm, ('ceramic', 'unfired_decorated_pot'), 'bsa:ceramic/unfired_decorated_pot', 0.8)

def create_item_models():
    print('Creating item models...')
    for color in COLORS:
        rm.item_model(('ceramic', 'jug', 'unfired', f'{color}')).with_lang(lang(f'{color} Unfired Jug')).with_lang(lang(f'{color} Unfired Jug'))
        contained_fluid(rm, ('ceramic', 'jug', 'glazed', f'{color}'), f'bsa:item/ceramic/jug/glazed/{color}', 'tfc:item/ceramic/jug_overlay').with_lang(lang(f'{color} Glazed Jug'))
        rm.item_model(('ceramic', 'pot', 'glazed', color), f'bsa:item/ceramic/pot/glazed/{color}').with_lang(lang(f'{color}_glazed_pot'))
        rm.item_model(('ceramic', 'pot', 'unfired', color), f'bsa:item/ceramic/pot/unfired/{color}').with_lang(lang(f'{color}_unfired_pot'))
        
    for rock_category in ROCK_CATEGORIES:
        rm.item_model(('stone', 'multitool_head', rock_category), 'bsa:item/stone/multitool_head').with_lang('Stone Multitool Head')
    
    rm.item_model(('clay_tablet'), 'bsa:item/clay_tablet')
    rm.item_model(('writeable_clay_tablet'), 'bsa:item/writeable_clay_tablet')
    rm.item_model(('written_clay_tablet'), 'bsa:item/written_clay_tablet')
    
    rm.item_model(('sinew'), 'bsa:item/sinew').with_lang(lang('sinew'))
    rm.item_model(('dried_sinew'), 'bsa:item/dried_sinew').with_lang(lang('dried_sinew'))
    rm.item_model(('pounded_sinew'), 'bsa:item/pounded_sinew').with_lang(lang('pounded_sinew'))
    rm.item_model(('sinew_string'), 'bsa:item/sinew_string').with_lang(lang('sinew_string'))
    
    rm.item_model(('bone', 'fishing_rod_cast'), 'bsa:item/bone/fishing_rod_cast', parent='minecraft:item/fishing_rod')
    item_model_property(rm, ('bone', 'fishing_rod'), [{'predicate': {'tfc:cast': 1}, 'model': 'bsa:item/bone/fishing_rod_cast'}], {'parent': 'minecraft:item/handheld_rod', 'textures': {'layer0': 'bsa:item/bone/fishing_rod'}}).with_lang('Bone Fishing Rod')
    rm.item_model(('bone', 'fish_hook'), 'bsa:item/bone/fish_hook').with_lang(lang('bone_fish_hook'))
    rm.item_model(('sabertooth_fang'), 'bsa:item/sabertooth_fang').with_lang(lang('sabertooth_fang'))
    
    for tool in STONE_TOOL_HEADS:
        if tool not in ('javelin', 'knife'):
            rm.item_model(('stone', tool, 'flint'), f'bsa:item/stone/flint/{tool}', parent='item/handheld').with_lang(lang(f'Flint {tool}'))
        rm.item_model(('stone', f'{tool}_head', 'flint'), f'bsa:item/stone/flint/{tool}_head').with_lang(lang(f'Flint {tool} Head'))
     
    make_javelin(rm, 'stone/javelin/flint', 'bsa:item/stone/flint/javelin').with_lang('Flint Javelin')
    rm.item_model(('stone', 'knife', 'flint'), f'bsa:item/stone/flint/knife', parent='tfc:item/handheld_flipped').with_lang('Flint Knife')
    
    
    rm.item_model(('stone', 'multitool_head', 'flint'), 'bsa:item/stone/flint/multitool_head').with_lang('Flint Multitool Head')
    rm.item_model(('stone', 'arrowhead'), 'bsa:item/stone/arrowhead').with_lang('Stone Arrowhead')
    rm.item_model(('stone', 'arrowhead', 'flint'), 'bsa:item/stone/flint/arrowhead').with_lang('Flint Arrowhead')
    rm.item_model(('bone', 'arrowhead'), 'bsa:item/bone/arrowhead').with_lang('Bone Arrowhead')
    rm.item_model(('hide_door'), 'bsa:item/hide_door')
    
    for grain in GRAINS:
        rm.item_model(('food', f'coarse_{grain}_flour'), f'bsa:item/food/coarse_{grain}_flour')
    
    for grain in NON_BROKEN_GRAINS:
        rm.item_model(('food', f'crushed_{grain}_grain'), f'bsa:item/food/crushed_{grain}_grain')
        
    rm.item_model(('food', 'porridge'), 'bsa:item/food/porridge')
    rm.item_model(('hide_door'), 'bsa:item/hide_door')
    rm.item_model(('dust', 'clay'), 'bsa:item/dust/clay').with_lang('Clay Dust')
    
    for pattern in SHERD_PATTERNS:
        rm.item_model(('ceramic', 'sherd', 'unfired', pattern), f'bsa:item/ceramic/sherd/unfired/{pattern}').with_lang(lang(f'{pattern}_unfired_sherd'))
    rm.item_model(('ceramic', 'sherd', 'fired', 'blank'), 'bsa:item/ceramic/sherd/fired/blank').with_lang(lang('blank_fired_sherd'))
    
def create_loot_tables():
    print('\tCreating loot tables...')
    tfc_rm.block_loot('tfc:calcite', {'name': 'tfc:powder/flux', 'functions': [utils.loot_functions({'function': 'minecraft:set_count', 'count': {'min': 1, 'max': 2, 'type': 'minecraft:uniform'}})]})
    tfc_rm.block_loot('tfc:charcoal_pile', {'type': 'minecraft:alternatives', 'children': [{'type': 'minecraft:item', 'name': 'tfc:powder/charcoal', 'conditions': [{'condition': 'minecraft:match_tool', 'predicate': {'tag': 'tfc:hammers'}}], 'functions': [{'function': 'minecraft:set_count', 'count': 2}]}, {'type': 'minecraft:item', 'name': 'minecraft:charcoal'}]})
    rm.block_loot('bsa:sinew', {'name': 'bsa:sinew', 'conditions': [loot_tables.block_state_property('bsa:sinew[dried=false]')]}, {'name': 'bsa:dried_sinew', 'conditions': [loot_tables.block_state_property('bsa:sinew[dried=true]')]})
    rm.block_loot('bsa:hide_door', {'name': 'bsa:hide_door', 'conditions': loot_tables.block_state_property('bsa:hide_door[half=lower]')})
    
    for rock_category in ROCKS:
        tfc_rm.block_loot(f'tfc:rock/gravel/{rock_category}', {'name': f'tfc:rock/gravel/{rock_category}'})
    
    
def create_loot_modifiers():
    print('Creating loot modifiers...')
    
    loot_modifiers = []
    loot_modifier_add_itemstack_min_max(rm, loot_modifiers, 'animals_drop_sinew', '#bsa:drops_sinew', 'bsa:sinew', 1, 3)
    loot_modifier_add_itemstack_min_max(rm, loot_modifiers, 'sabertooth_fangs', 'tfc:sabertooth', 'bsa:sabertooth_fang', 1, 2)
    
    forge_rm.data(('loot_modifiers', 'global_loot_modifiers'), {'replace': False, 'entries': loot_modifiers})


def create_loot():
    print('Creating loot...')
    create_loot_tables()
    create_loot_modifiers()


def create_misc_lang():
    print('Creating misc translations...')
    rm.lang('item.minecraft.string', 'Spider Silk')
    for color in COLORS:
        rm.lang(f'item.bsa.ceramic.jug.glazed.{color}.filled', '%s ' + lang(f'{color} Glazed Ceramic Jug'))
    for rock_category in ROCK_CATEGORIES:
        rm.lang(f'item.bsa.stone.multitool_head.{rock_category}', lang(f'Stone Multitool Head'))
    
    for grain in NON_BROKEN_GRAINS:
        rm.lang(f'item.bsa.food.crushed_{grain}_grain', lang(f'Crushed {grain} Grain'))
    
    for grain in GRAINS:
        rm.lang(f'item.bsa.food.coarse_{grain}_flour', lang(f'Coarse {grain} Flour'))
    
    rm.lang('item.bsa.food.porridge', 'Porridge')
    rm.lang('tfc.jei.porridge_pot', 'Porridge Pot')
    rm.lang('tfc.jei.bone_knapping', 'Bone Knapping')
    rm.lang('subtitle.item.bsa.knapping.bone', 'Bone Scrapes')
    rm.lang('block.bsa.sinew', 'Sinew')
    rm.lang('config.jade.plugin_tfc.drying_sinew', 'Drying Sinew')
    rm.lang('block.bsa.hide_door', 'Hide Door')

def create_anvil_recipes():
    print('\tCreating anvil recipes...')
    anvil_recipe(rm, ('pounded_sinew'), 'bsa:dried_sinew', 'bsa:pounded_sinew', 0, Rules.hit_third_last, Rules.hit_second_last, Rules.hit_last)
    
    for grain in NON_BROKEN_GRAINS:
        anvil_recipe(rm, ('food', f'crushed_{grain}_grain'), not_rotten(f'tfc:food/{grain}_grain'), item_stack_provider(f'bsa:food/crushed_{grain}_grain', copy_oldest_food=True), 0, Rules.hit_third_last, Rules.hit_second_last, Rules.hit_last)
        anvil_recipe(rm, ('food', f'coarse_{grain}_flour'), not_rotten(f'bsa:food/crushed_{grain}_grain'), item_stack_provider(f'bsa:food/coarse_{grain}_flour', copy_oldest_food=True), 0, Rules.draw_third_last, Rules.draw_second_last, Rules.draw_last)
    
    for grain in BROKEN_GRAINS:
        anvil_recipe(rm, ('food', f'coarse_{grain}_flour'), not_rotten(f'tfc:food/{grain}_grain'), item_stack_provider(f'bsa:food/coarse_{grain}_flour', copy_oldest_food=True), 0, Rules.draw_third_last, Rules.draw_second_last, Rules.draw_last)    

def create_barrel_recipes():
    print('\tCreating barrel recipes...')
    for color in COLORS:
        barrel_sealed_recipe(rm, ('ceramic', 'jug', 'unfired', color), f'Dyeing Unfired Jug {color}', 1000, 'tfc:ceramic/unfired_jug', f'25 tfc:{color}_dye', f'bsa:ceramic/jug/unfired/{color}')
        barrel_sealed_recipe(rm, ('ceramic', 'pot', 'unfired', color), f'Dyeing Unfired Pot {color}', 1000, 'tfc:ceramic/unfired_pot', f'25 tfc:{color}_dye', f'bsa:ceramic/pot/unfired/{color}')

def create_decorated_pot_recipes():
    print('\t\tCreating decorated pot recipes...')
    rm.recipe(('crafting', 'unfired_decorated_pot'), 'bsa:crafting_unfired_decorated_pot', {
        "category": "misc"
    })

def create_crafting_recipes():
    print('\tCreating crafting recipes...')
    
    print('\t\tCreating color recipes...')
    for color in COLORS:
        rm.crafting_shapeless(('crafting', 'ceramic', 'jug', 'unfired', f'{color}'), ('tfc:ceramic/unfired_jug', f'minecraft:{color.lower()}_dye'), f'bsa:ceramic/jug/unfired/{color}')
        rm.crafting_shapeless(('crafting', 'ceramic', 'jug', 'unfired', f'{color}_liquid_dye'), ('tfc:ceramic/unfired_jug', fluid_item_ingredient(f'100 tfc:{color}_dye')), f'bsa:ceramic/jug/unfired/{color}')
        rm.crafting_shapeless(('crafting', 'ceramic', 'pot', 'unfired', f'{color}'), ('tfc:ceramic/unfired_pot', f'minecraft:{color.lower()}_dye'), f'bsa:ceramic/pot/unfired/{color}')
        rm.crafting_shapeless(('crafting', 'ceramic', 'pot', 'unfired', f'{color}_liquid_dye'), ('tfc:ceramic/unfired_pot', fluid_item_ingredient(f'100 tfc:{color}_dye')), f'bsa:ceramic/pot/unfired/{color}')
        
        
        rm.crafting_shapeless(('crafting', 'ceramic', f'{color}_unfired_vessel_liquid_dye'), ('tfc:ceramic/unfired_vessel', fluid_item_ingredient(f'100 tfc:{color}_dye')), f'tfc:ceramic/{color}_unfired_vessel')
        rm.crafting_shapeless(('crafting', 'ceramic', f'{color}_unfired_large_vessel_liquid_dye'), ('tfc:ceramic/unfired_large_vessel', fluid_item_ingredient(f'100 tfc:{color}_dye')), f'tfc:ceramic/unfired_large_vessel/{color}')
        
    rm.crafting_shapeless(('crafting', 'dye', 'darken_blue'), ('minecraft:light_blue_dye', 'minecraft:black_dye'), utils.item_stack({'item': 'minecraft:blue_dye', 'count': 2}))
    rm.crafting_shapeless(('crafting', 'dye', 'darken_gray'), ('minecraft:light_gray_dye', 'minecraft:black_dye'), utils.item_stack({'item': 'minecraft:gray_dye', 'count': 2}))
    rm.crafting_shapeless(('crafting', 'dye', 'darken_green'), ('minecraft:lime_dye', 'minecraft:black_dye'), utils.item_stack({'item': 'minecraft:green_dye', 'count': 2}))
    rm.crafting_shapeless(('crafting', 'dye', 'darken_purple'), ('minecraft:magenta_dye', 'minecraft:black_dye'), utils.item_stack({'item': 'minecraft:purple_dye', 'count': 2}))
    
    rm.crafting_shapeless(('crafting', 'dye', 'white_from_flux'), 'tfc:powder/flux', 'minecraft:white_dye')
    rm.crafting_shapeless(('crafting', 'sinew_string'), 'bsa:pounded_sinew', 'bsa:sinew_string')
    
    print('\t\tCreating rock category-dependant recipes...')
    for rock_category in ROCK_CATEGORIES:
        for tool_type in STONE_TOOL_HEADS:
            rm.crafting_shaped(('crafting', 'stone', tool_type, rock_category, 'no_binding'), ['H', 'R'], {'H': f'tfc:stone/{tool_type}_head/{rock_category}', 'R': '#forge:rods/wooden'}, utils.item_stack({'item': f'tfc:stone/{tool_type}/{rock_category}', 'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES[rock_category] // 2}}))
            disable_recipe(rm, f'tfc:crafting/stone/{tool_type}_{rock_category}')
            
            for binding in STONE_TOOL_BINDINGS:
                rm.crafting_shaped(('crafting', 'stone', tool_type, rock_category, f'{binding}_binding'), ['BH', 'R '], {'B': f'#bsa:bindings/{binding}', 'H': f'tfc:stone/{tool_type}_head/{rock_category}', 'R': '#forge:rods/wooden'}, utils.item_stack(
                    {'item': f'tfc:stone/{tool_type}/{rock_category}', 'nbt': {'tfc:forging_bonus': BINDING_BONUSES[binding]}}))
        
        rm.crafting_shaped(('crafting', 'stone', 'axe',     rock_category, 'multitool'), ['RM'],       {'M': f'bsa:stone/multitool_head/{rock_category}', 'R': f'#forge:rods/wooden'}, utils.item_stack({'item': f'tfc:stone/axe/{rock_category}',     'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES[rock_category] // 2}}))
        rm.crafting_shaped(('crafting', 'stone', 'hammer',  rock_category, 'multitool'), [' R', 'M '], {'M': f'bsa:stone/multitool_head/{rock_category}', 'R': f'#forge:rods/wooden'}, utils.item_stack({'item': f'tfc:stone/hammer/{rock_category}',  'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES[rock_category] // 2}}))
        rm.crafting_shaped(('crafting', 'stone', 'hoe',     rock_category, 'multitool'), ['M', 'R'],   {'M': f'bsa:stone/multitool_head/{rock_category}', 'R': f'#forge:rods/wooden'}, utils.item_stack({'item': f'tfc:stone/hoe/{rock_category}',     'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES[rock_category] // 2}}))
        rm.crafting_shaped(('crafting', 'stone', 'shovel',  rock_category, 'multitool'), ['R', 'M'],   {'M': f'bsa:stone/multitool_head/{rock_category}', 'R': f'#forge:rods/wooden'}, utils.item_stack({'item': f'tfc:stone/shovel/{rock_category}',  'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES[rock_category] // 2}}))
        rm.crafting_shaped(('crafting', 'stone', 'javelin', rock_category, 'multitool'), [' M', 'R '], {'M': f'bsa:stone/multitool_head/{rock_category}', 'R': f'#forge:rods/wooden'}, utils.item_stack({'item': f'tfc:stone/javelin/{rock_category}', 'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES[rock_category] // 2}}))
        
        rm.crafting_shapeless(('crafting', 'stone', 'knife', rock_category, 'multitool'), (f'bsa:stone/multitool_head/{rock_category}',), utils.item_stack({'item': f'tfc:stone/knife_head/{rock_category}',   'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES[rock_category] // 2}}))
    
    print('\t\tCreating flint recipes...')
    
    for tool_type in STONE_TOOL_HEADS:
        rm.crafting_shaped(('crafting', 'stone', tool_type, 'flint', 'no_binding'), ['H', 'R'], {'H': f'bsa:stone/{tool_type}_head/flint', 'R': '#forge:rods/wooden'}, utils.item_stack({'item': f'bsa:stone/{tool_type}/flint', 'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES['flint'] // 2}}))
        for binding in STONE_TOOL_BINDINGS:
            rm.crafting_shaped(('crafting', 'stone', tool_type, 'flint', f'{binding}_binding'), ['BH', 'R '], {'B': f'#bsa:bindings/{binding}', 'H': f'bsa:stone/{tool_type}_head/flint', 'R': '#forge:rods/wooden'}, utils.item_stack(
                {'item': f'bsa:stone/{tool_type}/flint', 'nbt': {'tfc:forging_bonus': BINDING_BONUSES[binding]}}))
    
    rm.crafting_shaped(('crafting', 'stone', 'axe',     'flint', 'multitool'), ['RM'],       {'M': f'bsa:stone/multitool_head/flint', 'R': f'#forge:rods/wooden'}, utils.item_stack({'item': f'bsa:stone/axe/flint',     'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES['flint'] // 2}}))
    rm.crafting_shaped(('crafting', 'stone', 'hammer',  'flint', 'multitool'), [' R', 'M '], {'M': f'bsa:stone/multitool_head/flint', 'R': f'#forge:rods/wooden'}, utils.item_stack({'item': f'bsa:stone/hammer/flint',  'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES['flint'] // 2}}))
    rm.crafting_shaped(('crafting', 'stone', 'hoe',     'flint', 'multitool'), ['M', 'R'],   {'M': f'bsa:stone/multitool_head/flint', 'R': f'#forge:rods/wooden'}, utils.item_stack({'item': f'bsa:stone/hoe/flint',     'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES['flint'] // 2}}))
    rm.crafting_shaped(('crafting', 'stone', 'shovel',  'flint', 'multitool'), ['R', 'M'],   {'M': f'bsa:stone/multitool_head/flint', 'R': f'#forge:rods/wooden'}, utils.item_stack({'item': f'bsa:stone/shovel/flint',  'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES['flint'] // 2}}))
    rm.crafting_shaped(('crafting', 'stone', 'javelin', 'flint', 'multitool'), [' M', 'R '], {'M': f'bsa:stone/multitool_head/flint', 'R': f'#forge:rods/wooden'}, utils.item_stack({'item': f'bsa:stone/javelin/flint', 'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES['flint'] // 2}}))
    
    rm.crafting_shapeless(('crafting', 'stone', 'knife', 'flint', 'multitool'), (f'bsa:stone/multitool_head/flint',), utils.item_stack({'item': f'tfc:stone/knife_head/flint',   'nbt': {'Damage': ROCK_CATEGORY_DURABILITIES['flint'] // 2}}))    
    
    
    damage_shapeless(rm, ('crafting', 'hide_sewing', '1_plus_1'), ('tfc:small_raw_hide', 'tfc:small_raw_hide', '#forge:string', 'tfc:bone_needle'), "tfc:medium_raw_hide")
    damage_shapeless(rm, ('crafting', 'hide_sewing', '1_plus_2'), ('tfc:small_raw_hide', 'tfc:medium_raw_hide', '#forge:string', 'tfc:bone_needle'), "tfc:large_raw_hide")
    damage_shapeless(rm, ('crafting', 'hide_sewing', '2_plus_2'), ('tfc:medium_raw_hide', 'tfc:medium_raw_hide', '#forge:string', 'tfc:bone_needle'), "tfc:large_raw_hide")
    
    for grain in GRAINS:
        for i in range(2, 1 + 8, 2):
            advanced_shapeless(rm, f'crafting/dough/{grain}/{i}_from_coarse_flour', (fluid_item_ingredient('100 minecraft:water'), *repeat(not_rotten(f'bsa:food/coarse_{grain}_flour'), i)), item_stack_provider(f'{int(i / 2)} tfc:food/{grain}_dough', copy_oldest_food=True))
    
    disable_recipe(rm, f'tfc:crafting/bone_needle')
    damage_shapeless(rm, ('crafting', 'bone_needle'), ('bsa:sabertooth_fang', '#tfc:knives'), 'tfc:bone_needle')
    rm.crafting_shaped(('crafting', 'bone', 'fishing_rod'), ('RS', 'RH'), {'R': '#forge:rods/wooden', 'S': '#forge:string', 'H': 'bsa:bone/fish_hook'}, 'bsa:bone/fishing_rod')
    
    disable_recipe(rm, 'minecraft:arrow')
    rm.crafting_shaped(('crafting', 'arrow', 'stone'), ('A', 'R', 'F'), {'A': 'bsa:stone/arrowhead', 'R': '#forge:rods/wooden', 'F': 'minecraft:feather'}, 'minecraft:arrow')
    rm.crafting_shaped(('crafting', 'arrow', 'flint'), ('A', 'R', 'F'), {'A': 'bsa:stone/arrowhead/flint', 'R': '#forge:rods/wooden', 'F': 'minecraft:feather'}, (2, 'minecraft:arrow'))
    rm.crafting_shaped(('crafting', 'arrow', 'bone'), ('A', 'R', 'F'), {'A': 'bsa:bone/arrowhead', 'R': '#forge:rods/wooden', 'F': 'minecraft:feather'}, 'minecraft:arrow')
    rm.crafting_shaped(('crafting', 'hide_door'), ('WH', 'W '), {'W': 'tfc:wattle', 'H': 'tfc:large_raw_hide'}, 'bsa:hide_door')
    extra_products_shapeless(rm, ('crafting', 'hide_door_uncraft'), ('bsa:hide_door'), 'tfc:large_raw_hide', ('tfc:wattle', 'tfc:wattle'))
    for i in range(1, 8 + 1):
        advanced_shapeless(rm, ('crafting', f'clay_from_ceramic_dust_{i}'), (fluid_item_ingredient('100 minecraft:water'), *(['bsa:dust/clay'] * i)), utils.item_stack((i, 'minecraft:clay_ball')))
    
    damage_shapeless(rm, ('ceramic', 'dust_1'), (must_be_empty('#bsa:ceramic/smashable_1'), '#tfc:hammers'), (1, 'bsa:dust/clay'))
    damage_shapeless(rm, ('ceramic', 'dust_5'), (must_be_empty('#bsa:ceramic/smashable_5'), '#tfc:hammers'), (5, 'bsa:dust/clay'))
    
    create_decorated_pot_recipes()
    
    
def create_arrowhead_knapping_recipes():
    print('\t\tGenerating arrowhead knapping recipes...')
    placements = {
        ('XX XX', 'X   X'): 2,
        ('XX XX', 'X  X '): 2,
        ('XX XX', ' X  X'): 2,
        ('XX XX', ' X X '): 2,
        ('XX  X', 'X  XX'): 2,
        ('XX X ', 'X  XX'): 2,
        ('XX  X', ' X XX'): 2,
        ('XX X ', ' X XX'): 2,
        ('XX   ', 'X    '): 1,
        ('XX   ', ' X   '): 1,
        ('     ', '     '): 0
    }
    for top_part, name_part_1 in zip(placements, range(len(placements))):
        if name_part_1 == 5:
            print('\t\t\t\tHalfway done...')
        for bottom_part, name_part_2 in zip(placements, range(len(placements))):
            for top_mod, name_part_3 in zip((1, -1), range(2)):
                for bottom_mod, name_part_4 in zip((1, -1), range(2)):
                    rock_knapping(
                        rm,
                        ('stone', 'arrowhead', f'{name_part_1}_{name_part_2}_{name_part_3}_{name_part_4}'),
                        (*(top_part[::top_mod]), '     ', *(bottom_part[::bottom_mod])),
                        (placements[top_part] + placements[bottom_part], 'bsa:stone/arrowhead'),
                        '#bsa:rock/loose'
                    )
                    rock_knapping(
                        rm,
                        ('stone', 'arrowhead', 'flint', f'{name_part_1}_{name_part_2}_{name_part_3}_{name_part_4}'),
                        (*(top_part[::top_mod]), '     ', *(bottom_part[::bottom_mod])),
                        (placements[top_part] + placements[bottom_part], 'bsa:stone/arrowhead/flint'),
                        'minecraft:flint'
                    )
                    bone_knapping(
                        rm,
                        ('bone', 'arrowhead', f'{name_part_1}_{name_part_2}_{name_part_3}_{name_part_4}'),
                        (*(top_part[::top_mod]), '     ', *(bottom_part[::bottom_mod])),
                        (placements[top_part] + placements[bottom_part], 'bsa:bone/arrowhead'),
                        '#bsa:bone_knapping'
                    )

def create_heating_recipes():
    print('\tCreating heating recipes...')
    for color in COLORS:
        heat_recipe(rm, ('ceramic', 'jug', color), f'bsa:ceramic/jug/unfired/{color}', POTTERY_MELT, f'bsa:ceramic/jug/glazed/{color}')    
        heat_recipe(rm, ('ceramic', 'pot', color), f'bsa:ceramic/pot/unfired/{color}', POTTERY_MELT, f'bsa:ceramic/pot/glazed/{color}')
    heat_recipe(rm, ('ceramic', 'decorated_pot'), 'bsa:ceramic/unfired_decorated_pot', POTTERY_MELT, item_stack_provider('minecraft:decorated_pot', copy_sherds=True))
    
    
def create_bone_knapping_recipes():
    print('\t\tCreating bone knapping recipes...')
    
    knapping_type(rm, 'bone', '#bsa:bone_knapping', 1, 'bsa:item.knapping.bone', False, False, True, 'minecraft:bone')
    
    bone_knapping(rm, 'fish_hook', ['  X', '  X', '  X', 'X X', ' XX'], 'bsa:bone/fish_hook', '#bsa:bone_knapping')
    bone_knapping(rm, 'needle', ['   XX', '   XX', '  X  ', ' X   ', 'X    '], 'tfc:bone_needle', '#bsa:bone_knapping')
    
def create_rock_knapping_recipes():
    print('\t\tCreating rock knapping recipes...')
    for rock_category in ROCK_CATEGORIES:
        predicate = f'#tfc:{rock_category}_rock'
        rock_knapping(rm, ('stone', 'multitool_head', rock_category), ['  X  ', ' XXX ', ' XXX ', 'XXXXX', ' XXX '], f'bsa:stone/multitool_head/{rock_category}', predicate)
    
    rock_knapping(rm, ('stone', 'axe_head', 'flint'), [' X   ', 'XXXX ', 'XXXXX', 'XXXX ', ' X   '], 'bsa:stone/axe_head/flint', 'minecraft:flint')
    rock_knapping(rm, ('stone', 'hammer_head', 'flint'), ['XXXXX', 'XXXXX', '  X  '], 'bsa:stone/hammer_head/flint', 'minecraft:flint')
    
    rock_knapping(rm, ('stone', 'hoe_head', 'flint'), ['XXXXX', '   XX'], 'bsa:stone/hoe_head/flint', 'minecraft:flint')
    rock_knapping(rm, ('stone', 'hoe_head_1', 'flint'), ['XXXXX', '   XX', '     ', 'XXXXX', '   XX'], 'bsa:stone/hoe_head/flint', 'minecraft:flint')
    rock_knapping(rm, ('stone', 'hoe_head_2', 'flint'), ['XXXXX', '   XX', '     ', 'XXXXX', 'XX   '], 'bsa:stone/hoe_head/flint', 'minecraft:flint')
    
    rock_knapping(rm, ('stone', 'javelin_head', 'flint'), ['XXX  ', 'XXXX ', 'XXXXX', ' XXX ', '  X  '], 'bsa:stone/javelin_head/flint', 'minecraft:flint')
    
    rock_knapping(rm, ('stone', 'knife_head', 'flint'), [' X', 'XX', 'XX', 'XX', 'XX'], 'bsa:stone/knife_head/flint', 'minecraft:flint')
    rock_knapping(rm, ('stone', 'knife_head_1', 'flint'), ['X  X ', 'XX XX', 'XX XX', 'XX XX', 'XX XX'], (2, 'bsa:stone/knife_head/flint'), 'minecraft:flint')
    rock_knapping(rm, ('stone', 'knife_head_2', 'flint'), ['X   X', 'XX XX', 'XX XX', 'XX XX', 'XX XX'], (2, 'bsa:stone/knife_head/flint'), 'minecraft:flint')
    rock_knapping(rm, ('stone', 'knife_head_3', 'flint'), [' X X ', 'XX XX', 'XX XX', 'XX XX', 'XX XX'], (2, 'bsa:stone/knife_head/flint'), 'minecraft:flint')
    
    rock_knapping(rm, ('stone', 'shovel_head', 'flint'), [' XXX ', ' XXX ', ' XXX ', ' XXX ', '  X  '], 'bsa:stone/shovel_head/flint', 'minecraft:flint')
    rock_knapping(rm, ('stone', 'multitool_head', 'flint'), ['  X  ', ' XXX ', ' XXX ', 'XXXXX', ' XXX '], 'bsa:stone/multitool_head/flint', 'minecraft:flint')    

def create_knapping_recipes():
    print('\tCreating knapping recipes...')
    create_arrowhead_knapping_recipes()
    create_bone_knapping_recipes()
    create_rock_knapping_recipes()

def create_pot_recipes():
    print('\tCreating pot recipes...')
    for color in COLORS:
        for count in range(1, 1 + 5):
            simple_pot_recipe(rm, f'{color}_dye_from_flower_{count}', [utils.ingredient(f'#tfc:makes_{color}_dye')] * count, str(100 * count) + ' minecraft:water', str(100 * count) + f' tfc:{color}_dye', None, 2000, 730)
    
    grain_food = not_rotten(utils.ingredient('#bsa:foods/crushed_grains'))
    fruit_food = not_rotten(utils.ingredient('#tfc:foods/fruits'))
    for duration, ingredient_count in ((1000, 3), (1150, 4), (1300, 5)):
        for fruit_count in range(3):
            if (ingredient_count - fruit_count) >= 3:
                rm.recipe(('pot', f'porridge_{(ingredient_count - fruit_count)}_grains_{fruit_count}_fruits'), 'bsa:pot_porridge', {
                    'ingredients': ([grain_food] * (ingredient_count - fruit_count)) + ([fruit_food] * fruit_count),
                    'fluid_ingredient': fluid_stack_ingredient('100 minecraft:water'),
                    'duration': duration,
                    'temperature': 300
                })


def create_quern_recipes():
    print('\tCreating quern recipes...')
    for grain in NON_BROKEN_GRAINS:
        quern_recipe(rm, ('food', f'crushed_{grain}_grain'), f'bsa:food/crushed_{grain}_grain', item_stack_provider(f'tfc:food/{grain}_flour', copy_oldest_food=True))
    
    for grain in GRAINS:
        quern_recipe(rm, ('food', f'coarse_{grain}_flour'), f'bsa:food/coarse_{grain}_flour', item_stack_provider(f'tfc:food/{grain}_flour', copy_oldest_food=True))
    
    quern_recipe(rm, ('sabertooth_fang'), 'bsa:sabertooth_fang', 'minecraft:bone_meal', 2)
    
def create_recipes():
    print('Creating recipes...')
    create_anvil_recipes()
    create_barrel_recipes()
    create_crafting_recipes()
    create_heating_recipes()
    create_knapping_recipes()
    create_pot_recipes()
    create_quern_recipes()

def create_entity_tags():
    print('\tCreating entity tags...')
    rm.entity_tag('drops_sinew', 'tfc:orca', 'tfc:dolphin', 'tfc:manatee', 'tfc:grizzly_bear', 'tfc:polar_bear', 'tfc:black_bear', 'tfc:cougar', 'tfc:lion', 'tfc:sabertooth', 'tfc:tiger', 'tfc:crocodile', 'tfc:wolf', 'tfc:hyena', 'tfc:direwolf', 'tfc:pig', 'tfc:cow', 'tfc:goat', 'tfc:yak', 'tfc:alpaca', 'tfc:sheep', 'tfc:musk_ox', 'tfc:panda', 'tfc:deer', 'tfc:caribou', 'tfc:bongo', 'tfc:gazelle', 'tfc:boar', 'tfc:moose', 'tfc:wildebeest', 'tfc:donkey', 'tfc:mule', 'tfc:horse')

def create_item_tags():
    print('\tCreating item tags...')
    rm.item_tag('ceramic/unfired_jugs', 'tfc:ceramic/unfired_jug', *[f'bsa:ceramic/jug/unfired/{color}' for color in COLORS])
    rm.item_tag('ceramic/unfired_pots', 'tfc:ceramic/unfired_pot', *[f'bsa:ceramic/pot/unfired/{color}' for color in COLORS])
    rm.item_tag('ceramic/glazed_jugs', 'tfc:ceramic/jug', *[f'bsa:ceramic/jug/glazed/{color}' for color in COLORS])
    rm.item_tag('ceramic/glazed_pots', 'tfc:ceramic/pot', *[f'bsa:ceramic/pot/glazed/{color}' for color in COLORS])
    
    tfc_rm.item_tag('fluid_item_ingredient_empty_containers', '#bsa:ceramic/glazed_jugs')
    tfc_rm.fluid_tag('usable_in_jug', '#tfc:dyes')
    
    rm.item_tag('bindings/weak', 'tfc:straw', 'tfc:groundcover/dead_grass', 'tfc:glue')
    rm.item_tag('bindings/medium', 'tfc:jute', 'tfc:plant/ivy', 'tfc:plant/hanging_vines', 'tfc:plant/jungle_vines')
    rm.item_tag('bindings/strong', 'tfc:jute_fiber', '#forge:string')
    
    for rock_category in ROCK_CATEGORIES:
        tfc_rm.item_tag(f'{rock_category}_items', f'bsa:stone/multitool_head/{rock_category}')
    
    rm.item_tag('sinew_display', 'bsa:sinew', 'bsa:dried_sinew', 'bsa:pounded_sinew', 'bsa:sinew_string')
    
    forge_rm.item_tag('string', 'bsa:sinew_string')
    
    rm.item_tag('foods/crushed_grains', *[f'bsa:food/crushed_{grain}_grain' for grain in NON_BROKEN_GRAINS])
    tfc_rm.item_tag('dynamic_bowl_items', 'bsa:food/porridge')
    
    tfc_rm.item_tag('holds_small_fishing_bait', 'bsa:bone/fishing_rod')
    tfc_rm.item_tag('any_knapping', '#bsa:bone_knapping')
    tfc_rm.tag('usable_on_tool_rack', 'bsa:bone/fishing_rod')
    forge_rm.tag('fishing_rods', 'bsa:bone/fishing_rod')
    
    rm.item_tag('bone_knapping', 'minecraft:bone')
    tfc_rm.item_tag('rock_knapping', 'minecraft:flint')
    
    tfc_rm.item_tag('axes', 'bsa:stone/axe/flint')
    tfc_rm.item_tag('hammers', 'bsa:stone/hammer/flint')
    tfc_rm.item_tag('hoes', 'bsa:stone/hoe/flint')
    tfc_rm.item_tag('javelins', 'bsa:stone/javelin/flint')
    tfc_rm.item_tag('knives', 'bsa:stone/knife/flint')
    tfc_rm.item_tag('shovels', 'bsa:stone/shovel/flint')
    tfc_rm.item_tag('stone_tools', *[f'bsa:stone/{tool}_head/flint' for tool in STONE_TOOL_HEADS])
    tfc_rm.item_tag('inefficient_logging_axes', 'bsa:stone/axe/flint')
    
    rm.item_tag('rock/loose', *[f'tfc:rock/loose/{rock_type}' for rock_type in ROCKS])
    rm.item_tag('arrowheads', 'bsa:stone/arrowhead', 'bsa:stone/arrowhead/flint')
    rm.item_tag('tfc:usable_on_tool_rack', 'bsa:stone/shovel/flint', 'bsa:stone/javelin/flint', 'bsa:stone/knife/flint', 'bsa:stone/axe/flint', 'bsa:stone/hammer/flint', 'bsa:stone/hoe/flint', 'bsa:bone/fishing_rod', '#tfc:fluid_item_ingredient_empty_containers')
    
    rm.item_tag('ceramic/smashable_1', 'tfc:ceramic/bowl', 'minecraft:brick', 'minecraft:flower_pot', 'tfc:ceramic/ingot_mold')
    rm.item_tag('ceramic/smashable_5', 'tfc:ceramic/vessel', 'tfc:ceramic/large_vessel', 'tfc:ceramic/jug', 'tfc:ceramic/pot', 'tfc:ceramic/spindle_head', 'tfc:pan/empty', 'tfc:ceramic_blowpipe', "tfc:ceramic/axe_head_mold", "tfc:ceramic/chisel_head_mold", "tfc:ceramic/hammer_head_mold", "tfc:ceramic/hoe_head_mold", "tfc:ceramic/javelin_head_mold", "tfc:ceramic/knife_blade_mold", "tfc:ceramic/mace_head_mold", "tfc:ceramic/pickaxe_head_mold", "tfc:ceramic/propick_head_mold", "tfc:ceramic/saw_blade_mold", "tfc:ceramic/shovel_head_mold", "tfc:ceramic/sword_blade_mold", "tfc:ceramic/scythe_blade_mold", "tfc:ceramic/bell_mold")
    
    rm.item_tag('ceramic/unfired_sherds', *[f'bsa:ceramic/sherd/unfired/{pattern}' for pattern in SHERD_PATTERNS])
    
def create_worldgen_tags():
    print('Creating worldgen tags...')
    for biome in TFC_BIOMES:
        tfc_rm.placed_feature_tag((f'in_biome/surface_decoration/{biome}'), 'bsa:flint_patch')    
    
    
def create_tags():
    print('Creating tags...')
    create_entity_tags()
    create_item_tags()
    create_worldgen_tags()
    

def create_worldgen():
    print('Creating worldgen...')
    configured_patch_feature(rm, 'flint', patch_config('tfc:groundcover/flint[fluid=minecraft:water]', 1, 15, 5, 'fresh'), decorate_chance(6), decorate_square(), decorate_climate(-1000, 1000, 0, 1000))
    

def main():
    create_block_models()
    create_item_foods()
    create_item_heats()
    create_item_models()
    create_loot()
    create_misc_lang()
    create_recipes()
    create_tags()
    create_worldgen()
    
    
    forge_rm.flush()
    rm.flush()
    tfc_rm.flush()


if __name__ == '__main__':
    main()
    
