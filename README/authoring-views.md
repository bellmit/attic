# Authoring Views

In the context of this app, a "View" is a representation that can be rendered
to HTML. The rendering is handled by [Dropwizard
Views](https://dropwizard.github.io/dropwizard/manual/views.html), and
ultimately [FreeMarker](http://freemarker.org); for background, read their
respective documentation sites.

## Representations

Unless you have a _compelling_ reason otherwise, which you feel comfortable
putting to ink in the class comments of your `View` class and in your commit
message, extend `ViewConvention` in your view representation classes. This will
get you some niceties automatically.

For example, you'll get a free `links` attribute in your output JSON which
comes with the base URL of the app baked into it. You can even customize these
links yourself by subclassing `Links`. (Links in incoming JSON will be silently
ignored.)

Try to avoid one-top-level-attribute views: if you find yourself writing

    public class Profile extends ViewConvention {
        private User user;
    }

consider annotating the lone attribute with `@JsonUnwrapped` to embed it
directly into the response.

Login Box has Jersey's [declarative
linking](https://jersey.java.net/documentation/latest/declarative-linking.html)
feature enabled. Use this to create links wherever possible: it automatically
handles URL escaping, resolving links relative to the application base URL, and
lots of other goodies.

## Naming Conventions

As per Dropwizard Views' convention, views are named in parallel with their
underlying representation class. For example, if the representation class is
`com.example.api.Example`, the corresponding FreeMarker template should
be at `src/main/resources/com/example/api/example.ftl`. There's some
flexibility around names, but generally, convert `CamelCase` names to
`xml-spaced` names for the default view.

The intent is to make it relatively easy to find a given view, once you know
its representation (which you can find by inspecting the resources that match
URLs you're interested in).

## Escaping

It's 2015, and FreeMarker still doesn't have "escape by default" modes. Use the
following boilerplate to get the standard site layout _with HTML escaping_
enabled:

    <#ftl strip_whitespace=true>
    <#import '/ftl/ui.ftl' as ui>
    <#-- @ftlvariable name="" type="com.example.api.Foo" -->
    <#escape name as name?html>
    <@ui.page title="Surprise!">
        <#-- Your content here. -->
    </@ui.page>
    </#escape>

Note that `<#escape>` is _lexically_ scoped: if you're writing macros, apply it
to their bodies, even if they only occur within other `<#escape>` blocks, or
their bodies will _not_ be escaped.

If you're _sure_ that a value is safe, _and_ if you wish for your mistakes to
be visible to the world, then you can disable escaping on an
expression-by-expression basis:

    <#noescape>${foo}</#noescape>

That this is awkward to type is a feature, not a bug; if you're using
`<#noescape>` often enough to be annoyed by it, you probably want a different
tool.

## Libraries

FreeMarker macro and function libraries live in `src/main/resources/ftl`
(visible to templates as `/ftl` if imported as above).

Libraries may be namespaced (like Java packages); to support this, a hierarchal
library should import all of its children. See the `/ftl/ui.ftl` library for an
example.

Libraries (and _only_ libraries) need the following preamble:

    <#ftl strip_text=true strip_whitespace=true>

* The `strip_text` option causes text outside of macros and functions to be
  dropped at compilation time, preventing your clever remarks and typos from
  ending up in the final page.

* The `strip_whitespace` option limits the amount of whitespace damage in the
  output that sensible indentation in the source can cause. See the FreeMarker
  section on [stripping whitespace](http://freemarker.org/docs/dgui_misc_whitespace.html#dgui_misc_whitespace_stripping)
  for details.
