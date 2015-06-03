require 'slop'

module S3rve
  module Cli
    def self.usage(opts)
      puts opts
      exit 1
    end
    
    def self.options
      Slop.parse do |o|
        o.banner = "usage: s3rve [options] path/to/static.json"

        o.on '-?', '--help', "show this help message, then exit" do
          usage o
        end
      end
    end
  end
end
