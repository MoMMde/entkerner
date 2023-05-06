import secretstorage

connection = secretstorage.dbus_init()
collection = secretstorage.Collection(connection)
print("opened collection " + collection.get_label())
for item in collection.get_all_items():
    if item.unlock():
        print(f"Unlock failed! {item.get_label()}")
    print(f"item[created_at={item.get_created()}, label={item.get_label()}, attributed={item.get_attributes()}, secret={item.get_secret()}]")
    print()