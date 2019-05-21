# The Space Engineers Data Dump

The following information is extracted semi-automatically from the Space
Engineers data files, and can be presumed to be up to date.

## Blocks

Blocks are the basic unit of building ships and stations. They are welded
together from components using hand tools or ship welder blocks. (In Creative
mode, blocks can be placed directly.)

<details>
  <summary>Expand table of large blocks</summary>

| Name | Mass (kg) | PCU |
|:-----|----------:|----:|
{% for block in gamedata.blocks|selectattr('size', 'equalto', 'Large') -%}
| [{{ block.name|localize }}](Block-{{ block.id.type_id }}-{{ block.id.subtype_id }}) | {{ block|block_mass }} | {{ block.pcu }} |
{% endfor -%}

</details>

<details>
  <summary>Expand table of small blocks</summary>

| Name | Mass (kg) | PCU |
|:-----|----------:|----:|
{% for block in gamedata.blocks|selectattr('size', 'equalto', 'Small') -%}
| [{{ block.name|localize }}](Block-{{ block.id.type_id }}-{{ block.id.subtype_id }}) | {{ block|block_mass }} | {{ block.pcu }} |
{% endfor -%}

</details>

## Components

Components are the parts and materials that make up blocks. They are
manufactured from materials in a Survival Kit, Basic Assembler, or Assembler.

<details>
  <summary>Expand table of components</summary>

| Name | Mass (kg) | Volume (L) | Integrity (HP) |
|:-----|----------:|-----------:|---------------:|
{% for component in gamedata.components -%}
| [{{ component.name|localize }}](Component-{{ component.id.subtype_id }}) | {{ component.mass }} | {{ component.volume }} | {{ component.integrity }} |
{% endfor -%}

</details>
