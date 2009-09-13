import pygments
from pygments.lexers import get_lexer_by_name
from pygments.formatters import get_formatter_by_name
from pygments.styles import get_style_by_name

def highlight(context, body, syntax, format='html'):
    """Looks up the appropriate Pygments lexer for a given syntax
    and uses it to format the passed body.
    """
    lexer = get_lexer_by_name(syntax)
    formatter = get_formatter_by_name(format)
    
    return pygments.highlight(body, lexer, formatter)

def css_stylesheet(name, format='html'):
    """Returns the CSS associated with a Pygments Style."""
    formatter = get_formatter_by_name(format, style=name)
    
    return formatter.get_style_defs()
