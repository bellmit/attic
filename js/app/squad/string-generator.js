'use strict';

import { randomElement } from 'app/arrays';
import forOwn from 'lodash.forown';

function Literal(template) {
  this.template = template;
}
Object.assign(Literal.prototype, {
  expand() {
    return this.template;
  },
});

function Placeholder(name) {
  this.name = name;
}
Object.assign(Placeholder.prototype, {
  expand(generator) {
    return generator.generate(this.name);
  },
});

function Template(segments) {
  this.segments = segments;
}
Object.assign(Template.prototype, {
  expand(generator) {
    var segments = this.segments.map(segment => segment.expand(generator));
    return segments.join('');
  },
});

Template.parse = function parse(string) {
  /* have a tiny state machine */
  const SUBST_START = '{';
  const SUBST_END = '}';
  const LITERAL = {
    consider(c, segments, segment) {
      if (c == SUBST_START) { // skip in output, but see SUBST.remainder
        return {
          state: SUBST,
          segments: [...segments, new Literal(segment)],
          segment: "",
        }
      }

      return {
        state: LITERAL,
        segments,
        segment: segment + c,
      };
    },

    remainder(segments, segment) {
      return [...segments, new Literal(segment)];
    }
  };

  const SUBST = {
    consider(c, segments, segment) {
      if (c == SUBST_END) { // always skipped in output
        return {
          state: LITERAL,
          segments: [...segments, new Placeholder(segment)],
          segment: "",
        }
      }

      return {
        state: SUBST,
        segments,
        segment: segment + c,
      };
    },

    remainder(segments, segment) {
      // If we're in this state at the end of the string, we saw (and
      // discarded) a '{' that needs to be re-prepended.
      return [...segments, new Literal(SUBST_START + segment)];
    }
  };

  var state = LITERAL;
  var segments = [];
  var segment = "";
  for (var c of string) {
    ({state, segments, segment} = state.consider(c, segments, segment));
  }
  segments = state.remainder(segments, segment);
  return new Template(segments);
}

function StringGenerator() {
  this.rules = {};
}
Object.assign(StringGenerator.prototype, {
  generate(name) {
    var rules = this.rules[name];
    if (rules === undefined) {
      throw new Error("no templates");
    }
    return randomElement(rules).expand(this);
  },

  addRule(name, template) {
    var rules = this.rules[name] || [];
    var expander = Template.parse(template);
    rules.unshift(expander);
    this.rules[name] = rules;
  },
});

export function generator() {
  return new StringGenerator();
}

export function load(yaml) {
  var g = generator();
  forOwn(yaml.rules, (templates, rule) => {
    templates.forEach(template => g.addRule(rule, template));
  });
  return g;
}
