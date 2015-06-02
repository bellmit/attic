require 'slop'

module S3rve
  module Cli
    def self.options
      Slop.parse do |o|
        o.on '-?', '--help', "show this help message, then exit" do
          puts o
          exit
        end
      end
    end
  end
end
