require 'json'
require 'pathname'

module S3rve
  class Site
    def self.load(file)
      file = Pathname.new file
      directory = file.dirname

      config_json = File.read file
      config = JSON.load config_json
      
      return self.new directory, config
    end

    def initialize(basedir, config)
      @basedir = basedir
      @config = config
    end

    def document_root
      path = @config['root'] || 'public_html'
      @basedir.join path
    end
  end
end
