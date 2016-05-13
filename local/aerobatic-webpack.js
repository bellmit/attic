'use strict';

/*
 * Automatically add support for Aerobatic's CDN to Webpack's lazily-loaded
 * chunk handler. When the app is loaded from Aerobatic, this plugin will cause
 * the bundle to look in `options.clientConfigVar` (`__aerobatic__` by default)
 * for `staticAssetPath`, and will prepend it to the configured `publicPath` if
 * found.
 */
function AerobaticChunkLoader(options) {
  this.clientConfigVar = options.clientConfigVar || '__aerobatic__';
}

AerobaticChunkLoader.prototype = {
  apply: function apply(compiler) {
    var self = this;
    compiler.plugin('compilation', function(compilation) {
      // require-extensions is not documented - this plugin may break without
      // warning.
      compilation.mainTemplate.plugin('require-extensions', function(source, chunk, hash) {
        // source will contain the existing require-extensions block. We need to
        // append some extra code here to pick up Aerobatic's config and apply
        // it. Reuse the slightly-dismal code generation strategy used within
        // webpack, rather than inventing our own.
        //
        // We're going to blindly assume that the existing publicPath
        // (requireFn.p) value has the right number of slashes. It's easier to
        // be dumb and let users adjust their config than it is to do the
        // "right" thing and make it impossible for users to correct bad
        // guesses.
        var buf = [source];
        buf.push("");
        buf.push("// in an Aerobatic environment, use the CDN to load chunks");
        buf.push("if (typeof window !== 'undefined') {");
        buf.push("	if (typeof window." + self.clientConfigVar + " !== 'undefined') {");
        buf.push("		" + this.requireFn + ".p = (window." + self.clientConfigVar + ".staticAssetPath || '') + " + this.requireFn + ".p;");
        buf.push("	}");
        buf.push("}");
        return this.asString(buf);
      });
    });
  },
};

module.exports = AerobaticChunkLoader;
