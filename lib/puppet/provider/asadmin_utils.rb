module Puppet::Provider::AsadminUtils
  private
    # Converts a list of values into a hash of values by some key.
    def by_key(values)
      hashed = {}
      values.each do |value|
        key = yield(value)
        hashed[key] = value
      end
      hashed
    end
end
