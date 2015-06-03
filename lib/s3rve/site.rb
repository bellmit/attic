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

    def error_page
      @config['error_page']
    end

    def clean_urls
      @config['clean_urls'] || false
    end

    def clean_path(path)
      if clean_urls
        path.gsub /[.]html?$/, ''
      else
        path
      end
    end
  end
end
