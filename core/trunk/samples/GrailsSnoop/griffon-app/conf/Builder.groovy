root {
    'groovy.swing.SwingBuilder' {
        controller = ['Threading']
        view = "*"
    }
    'griffon.app.ApplicationBuilder' {
        view = "*"
    }
}

root.'griffon.builder.jide.JideBuilder'.view = '*'

jx {
    'groovy.swing.SwingXBuilder' {
        view = '*'
    }
}
