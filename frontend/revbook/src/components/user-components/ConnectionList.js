const ConnectionList = ({connections, title}) => {

    return(
        <>
        <h2>{title}</h2>
        {connections.length > 0 ? (
                <ul>
                    {connections.map((connection) => (
                        <li key={connection.id}>{connection.firstName} {connection.lastName}</li>
                    ))}
                </ul>
            ) : (
                <p>Nothing to display</p>
            )}
        </>
    );
}

export default ConnectionList;