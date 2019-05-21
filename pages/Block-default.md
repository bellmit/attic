# {{ block.name|localize }}

{% block details -%}
|   |   |
|:--|--:|
| Size   | {{ block.size }} |
| Width  | {{ block.width }} blocks |
| Height | {{ block.height }} blocks |
| Depth  | {{ block.depth }} blocks |
| Mass   | {{ block|block_mass }} kg |
| PCU    | {{ block.pcu }} |
{% endblock %}

{% block components -%}
## Components

| Component | Quantity | Mass (kg) |
|:----------|---------:|----------:|
{% for c in block.components -%}
{% set component = c.item_id|component -%}
| [{{ component.name|localize }}](Component-{{ component.id.subtype_id }}) | {{ c.quantity|int }} | {{ c.quantity * component.mass }} |
{% endfor -%}
{% endblock %}

{% block summary -%}
{% endblock %}
