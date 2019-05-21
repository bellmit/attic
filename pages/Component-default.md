# {{ component.name|localize }}

{% block details -%}
|   |   |
|:--|--:|
| Mass | {{ component.mass }} kg |
| Volume | {{ component.volume }} L |
| Integrity | {{ component.integrity }} HP |
{% endblock %}

{% block materials -%}
{% for blueprint in gamedata.blueprints.for_product(component.id) -%}

## Materials

Base production time: {{ blueprint.base_time }} seconds.

| Material | Quantity (kg) |
|:---------|--------------:|
{% for prereq in blueprint.prerequisites -%}
| {{ gamedata.physical_items.with_id(prereq.item_id).name|localize }} | {{ prereq.amount }} |
{% endfor -%}
{% endfor -%}
{% endblock %}

{% block summary -%}
{% endblock %}
