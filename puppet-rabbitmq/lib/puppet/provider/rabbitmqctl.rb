class Puppet::Provider::Rabbitmqctl < Puppet::Provider
  def self.rabbitmqctl_cmd(*args)
    rabbitmqctl '-q', *args
  end
  
  def rabbitmqctl_cmd(*args)
    self.class.rabbitmqctl_cmd *args
  end
  
  def self.by_name(items)
    by_key(items) { |item| item.name }
  end
  
  def self.by_key(items)
    hash_by_key = {}
    items.each do |item|
      key = yield item
      hash_by_key[key] = item
    end
    return hash_by_key
  end
end
