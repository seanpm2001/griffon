mvcGroups {
    root {
        model = 'GrailsSnoopModel'
        view = 'GrailsSnoopView'
        controller = 'GrailsSnoopController'
    }
}

application.title="Grails Snoop"
// The following properties have been added by the Upgrade process...
application.startupGroups=['root'] // default startup group from 0.0

// The following properties have been added by the Upgrade process...
application.autoShutdown=true // default autoShutdown from 0.0
